package com.maan.veh.claim.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.maan.veh.claim.entity.InsuredVehicleInfo;
import com.maan.veh.claim.entity.InsuredVehicleInfoId;



public interface InsuredVehicleInfoRepository extends JpaRepository<InsuredVehicleInfo, InsuredVehicleInfoId> {

	List<InsuredVehicleInfo> findByCompanyId(Integer companyId);

	List<InsuredVehicleInfo> findByCompanyIdAndStatus(Integer companyId, String status);

	List<InsuredVehicleInfo> findByCompanyIdAndStatusIn(Integer valueOf, List<String> status);

	List<InsuredVehicleInfo> findByClaimNoInAndStatusIn(List<String> claimNo, List<String> status);

	List<InsuredVehicleInfo> findByCompanyIdAndGarageId(Integer valueOf, String garageId);

	Optional<InsuredVehicleInfo> findByClaimNoAndGarageId(String claimNo, String garageId);

	List<InsuredVehicleInfo> findByClaimNoInAndGarageId(List<String> claimWithReplacement, String garageId);

	List<InsuredVehicleInfo> findByClaimNoIn(List<String> claimNumbers);

	List<InsuredVehicleInfo> findByClaimNoInAndGarageIdAndSurveyorId(List<String> claimWithReplacement, String garageId,
			String surveyorId);

	Optional<InsuredVehicleInfo> findByClaimNoAndGarageIdAndSurveyorId(String claimNo, String garageLoginId,
			String surveyorId);

	List<InsuredVehicleInfo> findByClaimNoInAndDealerId(List<String> claimNumbers, String sparepartsDealerId);

	List<InsuredVehicleInfo> findByCompanyIdAndGarageIdOrderByEntryDateDesc(Integer valueOf, String garageId);

	@Query("SELECT i.id FROM InsuredVehicleInfo i WHERE i.id IN :insuredIds")
	Set<InsuredVehicleInfoId> findExistingIds(@Param("insuredIds") Set<InsuredVehicleInfoId> insuredIds);

	List<InsuredVehicleInfo> findByClaimNoInAndSurveyorId(List<String> batch, String string);

	List<InsuredVehicleInfo> findByCompanyIdAndSurveyorId(Integer companyId, String surveyorLoginId);

	InsuredVehicleInfo findByClaimNoAndQuotationNo(String claimNo, String quotationNo);


	List<InsuredVehicleInfo> findByClaimNo(String claimNo);

	List<InsuredVehicleInfo> findByVehicleRegNo(String vehicleRegNo);

	List<InsuredVehicleInfo> findByGarageIdAndClaimNo(String garageId, String claimNo);

	List<InsuredVehicleInfo> findByGarageIdAndClaimNoAndVehicleRegNo(String garageId, String claimNo,
			String vehicleRegNo);

	List<InsuredVehicleInfo> findByGarageId(String garageId);

	



   
}