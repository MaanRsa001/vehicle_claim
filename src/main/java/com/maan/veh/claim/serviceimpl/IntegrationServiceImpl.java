package com.maan.veh.claim.serviceimpl;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.maan.veh.claim.entity.DamageSectionDetails;
import com.maan.veh.claim.entity.GarageWorkOrder;
import com.maan.veh.claim.entity.InsuredVehicleInfo;
import com.maan.veh.claim.entity.TotalAmountDetails;
import com.maan.veh.claim.repository.DamageSectionDetailsRepository;
import com.maan.veh.claim.repository.GarageWorkOrderRepository;
import com.maan.veh.claim.repository.InsuredVehicleInfoRepository;
import com.maan.veh.claim.repository.TotalAmountDetailsRepository;
import com.maan.veh.claim.request.IntegrationRequest;
import com.maan.veh.claim.service.IntegrationService;

@Service
public class IntegrationServiceImpl implements IntegrationService {

	private Logger log = LogManager.getLogger(IntegrationServiceImpl.class);

	@Autowired
	private InsuredVehicleInfoRepository insuredVehicleInfoRepository;

	@Autowired
	private GarageWorkOrderRepository garageWorkOrderRepository;

	@Autowired
	private TotalAmountDetailsRepository totalAmountDetailsRepository;

	@Autowired
	private DamageSectionDetailsRepository damageSectionDetailsRepository;

	@Value("${external.api.url}")
	private String externalApiUrl;

	@Override
	public Map<String, Object> getVehicleInfo(List<String> claimNoList) {
		Map<String, Object> response = new HashMap<>();
		try {
			List<Map<String, Object>> lpoInfo = new ArrayList<>();
			for (String claimNo : claimNoList) {
				// Fetch data from all tables
				Optional<InsuredVehicleInfo> insuredVehicleInfoOpt = insuredVehicleInfoRepository
						.findByClaimNo(claimNo);
				Optional<GarageWorkOrder> garageWorkOrderOpt = garageWorkOrderRepository.findByClaimNo(claimNo);
				Optional<TotalAmountDetails> totalAmountDetailsOpt = totalAmountDetailsRepository
						.findByClaimNo(claimNo);
				List<DamageSectionDetails> damageSectionDetailsList = damageSectionDetailsRepository
						.findByClaimNo(claimNo);
				// Prepare vehicleInfo
				Map<String, Object> vehicleDetails = new HashMap<>();
				// Map InsuredVehicleInfo to vehicleInfo
				insuredVehicleInfoOpt.ifPresent(info -> {
					vehicleDetails.put("vehicleMakeModel", info.getVehicleMake() + " " + info.getVehicleModel());
					vehicleDetails.put("makeYr", Optional.ofNullable(info.getMakeYear()).orElse(0));
					vehicleDetails.put("chassisNo", Optional.ofNullable(info.getChassisNo()).orElse(""));
					vehicleDetails.put("insuredClaimantName", Optional.ofNullable(info.getInsuredName()).orElse(""));
					vehicleDetails.put("type", Optional.ofNullable(info.getType()).orElse(""));
					vehicleDetails.put("vehicleReg", Optional.ofNullable(info.getVehicleRegNo()).orElse(""));
				});
				// Map GarageWorkOrder to garageWorkOrderInfo
				Map<String, Object> garageWorkOrderInfo = new HashMap<>();
				garageWorkOrderOpt.ifPresent(order -> {
					garageWorkOrderInfo.put("workOrderType", Optional.ofNullable(order.getWorkOrderType()).orElse(""));
					garageWorkOrderInfo.put("workOrderNo", Optional.ofNullable(order.getWorkOrderNo()).orElse(""));
					garageWorkOrderInfo.put("workOrderDate",
							Optional.ofNullable(order.getWorkOrderDate()).orElse(new Date()));
					garageWorkOrderInfo.put("accForSettlementType",
							Optional.ofNullable(order.getSettlementType()).orElse(""));
					garageWorkOrderInfo.put("accForSettlement",
							Optional.ofNullable(order.getSettlementTo()).orElse(""));
					garageWorkOrderInfo.put("sparePartsDealer",
							Optional.ofNullable(order.getSparepartsDealerId()).orElse(""));
					garageWorkOrderInfo.put("garageCode", Optional.ofNullable(order.getGarageId()).orElse(""));
					garageWorkOrderInfo.put("garageQuotationNo",
							Optional.ofNullable(order.getQuotationNo()).orElse(""));
					garageWorkOrderInfo.put("deliveryDate",
							Optional.ofNullable(order.getDeliveryDate()).orElse(new Date()));
					garageWorkOrderInfo.put("deliveryTo", Optional.ofNullable(order.getLocation()).orElse(""));
					garageWorkOrderInfo.put("subrogationYN", Optional.ofNullable(order.getSubrogationYn()).orElse(""));
					garageWorkOrderInfo.put("jointOrderYN", Optional.ofNullable(order.getJointOrderYn()).orElse(""));
					garageWorkOrderInfo.put("totalLoss",
							Optional.ofNullable(order.getTotalLoss()).orElse(BigDecimal.ZERO));
					garageWorkOrderInfo.put("totalLossType", Optional.ofNullable(order.getLossType()).orElse(""));
					garageWorkOrderInfo.put("remarks", Optional.ofNullable(order.getRemarks()).orElse(""));
				});
				// Map TotalAmountDetails to totalAmount
				Map<String, Object> totalAmount = new HashMap<>();
				totalAmountDetailsOpt.ifPresent(details -> {
					totalAmount.put("netAmount", Optional.ofNullable(details.getNetAmount()).orElse(BigDecimal.ZERO));
					totalAmount.put("unknownAccidentDeduction",
							Optional.ofNullable(details.getTotamtAftDeduction()).orElse(BigDecimal.ZERO));
					totalAmount.put("amountToBeRecovered",
							Optional.ofNullable(details.getTotamtWithVat()).orElse(BigDecimal.ZERO)); // Assumed
					totalAmount.put("totalAfterDeductions",
							Optional.ofNullable(details.getTotamtAftDeduction()).orElse(BigDecimal.ZERO));
					totalAmount.put("vatRatePer",
							Optional.ofNullable(details.getVatRatePercent()).orElse(BigDecimal.ZERO));
					totalAmount.put("vatRate", Optional.ofNullable(details.getVatRate()).orElse(BigDecimal.ZERO));
					totalAmount.put("vatAmount", Optional.ofNullable(details.getVatAmount()).orElse(BigDecimal.ZERO));
					totalAmount.put("totalWithVat",
							Optional.ofNullable(details.getTotamtWithVat()).orElse(BigDecimal.ZERO));
				});
				// Map TotalAmountDetails to replacementDetails and repairLabourDetails
				Map<String, Object> replacementDetails = new HashMap<>();
				Map<String, Object> repairLabourDetails = new HashMap<>();
				totalAmountDetailsOpt.ifPresent(details -> {
					replacementDetails.put("replacementCost",
							Optional.ofNullable(details.getTotReplaceCost()).orElse(BigDecimal.ZERO));
					replacementDetails.put("replacementCostDeductible",
							Optional.ofNullable(details.getTotReplaceCost()).orElse(BigDecimal.ZERO));
					replacementDetails.put("sparePartDepreciation",
							Optional.ofNullable(details.getTotReplaceCost()).orElse(BigDecimal.ZERO));
					replacementDetails.put("discountOnSpareParts",
							Optional.ofNullable(details.getTotReplaceCost()).orElse(BigDecimal.ZERO));
					replacementDetails.put("totalAmountReplacement",
							Optional.ofNullable(details.getTotReplaceCost()).orElse(BigDecimal.ZERO));

					repairLabourDetails.put("repairLabour",
							Optional.ofNullable(details.getTotLabourCost()).orElse(BigDecimal.ZERO));
					repairLabourDetails.put("repairLabourDeductible",
							Optional.ofNullable(details.getTotLabourCost()).orElse(BigDecimal.ZERO));
					repairLabourDetails.put("repairLabourDiscountAmount",
							Optional.ofNullable(details.getTotLabourCost()).orElse(BigDecimal.ZERO));
					repairLabourDetails.put("totalAmountRepairLabour",
							Optional.ofNullable(details.getTotLabourCost()).orElse(BigDecimal.ZERO));
				});
				// Map DamageSectionDetails to vehicleDamageDetails
				List<Map<String, Object>> vehicleDamageDetailsList = new ArrayList<>();
				for (DamageSectionDetails damageSection : damageSectionDetailsList) {
					Map<String, Object> vehicleDamageDetails = new HashMap<>();
					vehicleDamageDetails.put("damageDirection",
							Optional.ofNullable(damageSection.getDamageDirection()).orElse(""));
					vehicleDamageDetails.put("partyType",
							Optional.ofNullable(damageSection.getDamagePart()).orElse(""));
					vehicleDamageDetails.put("replaceRepair",
							Optional.ofNullable(damageSection.getRepairReplace()).orElse(""));
					vehicleDamageDetails.put("noUnits", Optional.ofNullable(damageSection.getNoOfParts()).orElse(0));
					vehicleDamageDetails.put("unitPrice",
							Optional.ofNullable(damageSection.getGaragePrice()).orElse(BigDecimal.ZERO));
					vehicleDamageDetails.put("repalcementCharge",
							Optional.ofNullable(damageSection.getReplaceCost()).orElse(BigDecimal.ZERO));
					vehicleDamageDetails.put("total",
							Optional.ofNullable(damageSection.getTotPrice()).orElse(BigDecimal.ZERO));

					vehicleDamageDetailsList.add(vehicleDamageDetails);
				}
				vehicleDetails.put("garageWorkOrderInfo", garageWorkOrderInfo);
				vehicleDetails.put("replacementDetails", replacementDetails);
				vehicleDetails.put("repairLabourDetails", repairLabourDetails);
				vehicleDetails.put("totalAmount", totalAmount);
				vehicleDetails.put("vehicleDamageDetails", vehicleDamageDetailsList);
				// Add to response structure
				Map<String, Object> lpoInfoItem = new HashMap<>();
				lpoInfoItem.put("vehicleInfo", vehicleDetails);
				lpoInfo.add(lpoInfoItem);
			}
			Map<String, Object> data = new HashMap<>();
			data.put("lpoInfo", lpoInfo);

			response.put("hasError", false);
			response.put("status", 200);
			response.put("data", data);

		} catch (Exception e) {
			response.put("hasError", true);
			response.put("status", 500);
			response.put("data", Collections.emptyMap());
			response.put("errorMessage", "An error occurred while processing the request: " + e.getMessage());
		}

		return response;
	}

	@Override
	public Map<String, Object> sendDataToExternalApi(IntegrationRequest request) {
		Map<String, Object> responseBody = new HashMap<>();
		try {
			// Prepare the request payload
			Map<String, Object> requestPayload = getVehicleInfo(request.getClaimNo());

			log.info("Vehicle Info Response ====> " + requestPayload);

			// Set up HTTP headers
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
			HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestPayload, headers);

			// Send POST request to external API
			String externalApiUrl = this.externalApiUrl;

			RestTemplate temp = new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(5))
					.setReadTimeout(Duration.ofSeconds(5)).build();

			ResponseEntity<String> response = temp.exchange(externalApiUrl, HttpMethod.POST, requestEntity,
					String.class);

			// Handle response
			responseBody.put("response", response.getBody());
			responseBody.put("statusCode", response.getStatusCodeValue());
			responseBody.put("status", response.getStatusCode().is1xxInformational());
			responseBody.put("hasError", response.getStatusCode().isError());

		} catch (Exception e) {
			// Handle exceptions
			responseBody.put("hasError", true);
			responseBody.put("status", 500);
			responseBody.put("message", "An error occurred: " + e.getMessage());
		}

		return responseBody;
	}

}
