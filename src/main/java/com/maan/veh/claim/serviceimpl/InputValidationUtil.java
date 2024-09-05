package com.maan.veh.claim.serviceimpl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maan.veh.claim.auth.passwordEnc;
import com.maan.veh.claim.entity.LoginMaster;
import com.maan.veh.claim.repository.LoginMasterRepository;
import com.maan.veh.claim.request.DamageSectionDetailsSaveReq;
import com.maan.veh.claim.request.LoginRequest;
import com.maan.veh.claim.response.ErrorList;
import com.maan.veh.claim.response.GarageWorkOrderSaveReq;

@Component
public class InputValidationUtil {
	
	@Autowired
	private LoginMasterRepository loginRepo;
	
	private static SimpleDateFormat DD_MM_YYYY = new SimpleDateFormat("dd/MM/yyyy");
	
	
	public List<ErrorList> isValidUser(LoginRequest req) {
		List<ErrorList> list = new ArrayList<ErrorList>();
		if(StringUtils.isBlank(req.getLoginId()))
			list.add(new ErrorList("100","LoginId","Please enter loginId"));	
		
		if(StringUtils.isBlank(req.getPassword()))
			list.add(new ErrorList("100","LoginId","Please enter password"));
		
		if(StringUtils.isNotBlank(req.getLoginId()) && StringUtils.isNotBlank(req.getPassword())) {
			String loginId = req.getLoginId();
			String password =new passwordEnc().crypt(req.getPassword().trim());
			LoginMaster lmaster =loginRepo.findByLoginIdIgnoreCaseAndPassword(loginId, password);
			if(lmaster==null){
				list.add(new ErrorList("100","LoginId","Please enter valid username/password"));
			}else {
				LoginMaster data =loginRepo.findByLoginIdIgnoreCaseAndPasswordAndStatus(loginId, password, "Y");
				if(data==null)
					list.add(new ErrorList("100","LoginId","Your account has been locked due to invalid password attempts."));

			}
			
		}
		
		return list;
		// TODO Auto-generated method stub
		
	}
	
	public List<ErrorList> validateWorkOrder(GarageWorkOrderSaveReq req){
		List<ErrorList> list = new ArrayList<ErrorList>();
		
		// Validate and set claimNo
        if (StringUtils.isBlank(req.getClaimNo())) {
            list.add(new ErrorList("100","Claim number","Claim number cannot be blank"));
        }

        // Validate and set workOrderNo
        if (StringUtils.isBlank(req.getWorkOrderNo())) {
            list.add(new ErrorList("100","WorkOrderNo","Work order number cannot be blank"));

        }

        // Validate and set workOrderType
        if (StringUtils.isBlank(req.getWorkOrderType())) {
            list.add(new ErrorList("100","WorkOrderType","Work order type cannot be blank"));
        }

        // Validate and set settlementType
        if (StringUtils.isBlank(req.getSettlementType())) {
            list.add(new ErrorList("100","SettlementType","Settlement type cannot be blank"));

        }

        // Validate and set settlementTo
        if (StringUtils.isBlank(req.getSettlementTo())) {
            list.add(new ErrorList("100","SettlementTo","Settlement to cannot be blank"));

        }

        // Validate and set garageName
        if (StringUtils.isBlank(req.getGarageName())) {
            list.add(new ErrorList("100","GarageName","Garage name cannot be blank"));

        }

        // Validate and set location
        if (StringUtils.isBlank(req.getLocation())) {
            list.add(new ErrorList("100","Location","Location cannot be blank"));

        }

        // Validate and set repairType
        if (StringUtils.isBlank(req.getRepairType())) {
            list.add(new ErrorList("100","RepairType","Repair type cannot be blank"));

        }

        // Validate and set quotationNo
        if (StringUtils.isBlank(req.getQuotationNo())) {
            list.add(new ErrorList("100","QuotationNo","Quotation number cannot be blank"));

        }

        // Validate and set lossType
        if (StringUtils.isBlank(req.getLossType())) {
            list.add(new ErrorList("100","Loss type","Loss type cannot be blank"));

        }

        // Validate and set createdBy
        if (StringUtils.isBlank(req.getCreatedBy())) {
            list.add(new ErrorList("100","CreatedBy","Created by cannot be blank"));

        }
        
        try {
            if (!StringUtils.isBlank(req.getWorkOrderDate())) {
                DD_MM_YYYY.parse(req.getWorkOrderDate());
            }
        } catch (ParseException e) {
            list.add(new ErrorList("100", "WorkOrderDate", "Work order date is invalid or not in the correct format (dd/MM/yyyy)"));
        }
        
        try {
            if (!StringUtils.isBlank(req.getDeliveryDate())) {
                DD_MM_YYYY.parse(req.getDeliveryDate());
            }
        } catch (ParseException e) {
            list.add(new ErrorList("100", "Delivery Date", "Delivery Date is invalid or not in the correct format (dd/MM/yyyy)"));
        }
        
        try {
            if (!StringUtils.isBlank(req.getTotalLoss())) {
                new BigDecimal(req.getTotalLoss());
            }
        } catch (NumberFormatException e) {
            list.add(new ErrorList("100", "TotalLoss", "Total loss must be a valid number"));
        }

       
		
		return list;
	}

	public List<ErrorList> validateDamageDetails(List<DamageSectionDetailsSaveReq> reqList) {
	    List<ErrorList> list = new ArrayList<>();
	    int line = 1;
	    for (DamageSectionDetailsSaveReq req : reqList) {
	    	
			if (StringUtils.isBlank(req.getClaimNo())) {
				list.add(new ErrorList("100", "ClaimNo", "Claim number cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getQuotationNo())) {
				list.add(new ErrorList("101", "QuotationNO", "Quotation number cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getDamageDirection())) {
				list.add(new ErrorList("102", "DamageDirection", "Damage direction cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getDamagePart())) {
				list.add(new ErrorList("103", "DamagePart", "Damage part cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getRepairReplace())) {
				list.add(new ErrorList("104", "RepairReplace", "Repair/Replace field cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getNoOfParts())) {
				list.add(new ErrorList("105", "NoOfParts", "Number of parts cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getGaragePrice())) {
				list.add(new ErrorList("106", "GaragePrice", "Garage price cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getDealerPrice())) {
				list.add(new ErrorList("107", "DealerPrice", "Dealer price cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getGarageLoginId())) {
				list.add(new ErrorList("108", "GarageLoginId", "Garage login ID cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getDealerLoginId())) {
				//list.add(new ErrorList("109", "DealerLoginId", "Dealer login ID cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getSurveyorId())) {
				//list.add(new ErrorList("110", "SurveyorId", "Surveyor ID cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getReplaceCost())) {
				list.add(new ErrorList("111", "ReplaceCost", "Replace cost cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getReplaceCostDeduct())) {
				list.add(new ErrorList("112", "ReplaceCostDeduct", "Replace cost deduction cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getSparepartDeprection())) {
				list.add(new ErrorList("113", "SparepartDeprection", "Spare part depreciation cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getDiscountSparepart())) {
				list.add(new ErrorList("114", "DiscountSparepart", "Discount on spare parts cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getTotamtReplace())) {
				list.add(new ErrorList("115", "TotamtReplace", "Total amount for replacement cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getLabourCost())) {
				list.add(new ErrorList("116", "LabourCost", "Labour cost cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getLabourCostDeduct())) {
				list.add(new ErrorList("117", "LabourCostDeduct", "Labour cost deduction cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getLabourDisc())) {
				list.add(new ErrorList("118", "LabourDisc", "Labour discount cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getTotamtOfLabour())) {
				list.add(new ErrorList("119", "TotamtOfLabour", "Total amount of labour cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getTotPrice())) {
				list.add(new ErrorList("120", "TotPrice", "Total price cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getStatus())) {
				//list.add(new ErrorList("122", "Status", "Status cannot be blank in line number : "+line));
			}

	        // Validate number formats and handle exceptions
	        if (StringUtils.isNotBlank(req.getNoOfParts())) {
	            try {
	                Integer.valueOf(req.getNoOfParts());
	            } catch (NumberFormatException e) {
	                list.add(new ErrorList("105", "NoOfParts", "Invalid number format for NoOfParts in line number : " + line));
	            }
	        }

	        if (StringUtils.isNotBlank(req.getGaragePrice())) {
	            try {
	                new BigDecimal(req.getGaragePrice());
	            } catch (NumberFormatException e) {
	                list.add(new ErrorList("106", "GaragePrice", "Invalid format for GaragePrice in line number : " + line));
	            }
	        }

	        if (StringUtils.isNotBlank(req.getDealerPrice())) {
	            try {
	                new BigDecimal(req.getDealerPrice());
	            } catch (NumberFormatException e) {
	                list.add(new ErrorList("107", "DealerPrice", "Invalid format for DealerPrice in line number : " + line));
	            }
	        }

	        if (StringUtils.isNotBlank(req.getReplaceCost())) {
	            try {
	                new BigDecimal(req.getReplaceCost());
	            } catch (NumberFormatException e) {
	                list.add(new ErrorList("111", "ReplaceCost", "Invalid format for ReplaceCost in line number : " + line));
	            }
	        }

	        if (StringUtils.isNotBlank(req.getReplaceCostDeduct())) {
	            try {
	                new BigDecimal(req.getReplaceCostDeduct());
	            } catch (NumberFormatException e) {
	                list.add(new ErrorList("112", "ReplaceCostDeduct", "Invalid format for ReplaceCostDeduct in line number : " + line));
	            }
	        }

	        if (StringUtils.isNotBlank(req.getSparepartDeprection())) {
	            try {
	                new BigDecimal(req.getSparepartDeprection());
	            } catch (NumberFormatException e) {
	                list.add(new ErrorList("113", "SparepartDeprection", "Invalid format for SparepartDeprection in line number : " + line));
	            }
	        }

	        if (StringUtils.isNotBlank(req.getDiscountSparepart())) {
	            try {
	                new BigDecimal(req.getDiscountSparepart());
	            } catch (NumberFormatException e) {
	                list.add(new ErrorList("114", "DiscountSparepart", "Invalid format for DiscountSparepart in line number : " + line));
	            }
	        }

	        if (StringUtils.isNotBlank(req.getTotamtReplace())) {
	            try {
	                new BigDecimal(req.getTotamtReplace());
	            } catch (NumberFormatException e) {
	                list.add(new ErrorList("115", "TotamtReplace", "Invalid format for TotamtReplace in line number : " + line));
	            }
	        }

	        if (StringUtils.isNotBlank(req.getLabourCost())) {
	            try {
	                new BigDecimal(req.getLabourCost());
	            } catch (NumberFormatException e) {
	                list.add(new ErrorList("116", "LabourCost", "Invalid format for LabourCost in line number : " + line));
	            }
	        }

	        if (StringUtils.isNotBlank(req.getLabourCostDeduct())) {
	            try {
	                new BigDecimal(req.getLabourCostDeduct());
	            } catch (NumberFormatException e) {
	                list.add(new ErrorList("117", "LabourCostDeduct", "Invalid format for LabourCostDeduct in line number : " + line));
	            }
	        }

	        if (StringUtils.isNotBlank(req.getLabourDisc())) {
	            try {
	                new BigDecimal(req.getLabourDisc());
	            } catch (NumberFormatException e) {
	                list.add(new ErrorList("118", "LabourDisc", "Invalid format for LabourDisc in line number : " + line));
	            }
	        }

	        if (StringUtils.isNotBlank(req.getTotamtOfLabour())) {
	            try {
	                new BigDecimal(req.getTotamtOfLabour());
	            } catch (NumberFormatException e) {
	                list.add(new ErrorList("119", "TotamtOfLabour", "Invalid format for TotamtOfLabour in line number : " + line));
	            }
	        }

	        if (StringUtils.isNotBlank(req.getTotPrice())) {
	            try {
	                new BigDecimal(req.getTotPrice());
	            } catch (NumberFormatException e) {
	                list.add(new ErrorList("120", "TotPrice", "Invalid format for TotPrice in line number : " + line));
	            }
	        }
	        
			line++;
		}
		return list;
	}


// 4351
	private int[]  mergeSort(int[] array) {
		
		if(array.length == 1)
			return array;
			
		int mid = array.length/2;
		
		int[] l = mergeSort(rangeOfArray(array,0,mid));// 43
		int[] r =mergeSort(rangeOfArray(array,mid,array.length));//3
		
		
		return merge(l,r);

	}
	
	private int[] merge(int[] l, int[] r) {
	int[] m = new int[l.length + r.length];
	int i = 0,j=0,k=0;
	
	while(i<l.length && j<r.length) {
		int val_1 =l[i];
		int val_2 =r[j];
		
		if(val_1<val_2) {
			m[k++] = l[i++] ;
		}else {
			m[k++] = r[j++] ;
		}
	}
	
	 while(i<l.length) {
		 m[k++] = l[i++];
	 }
	 
	 while(j<r.length) {
		 m[k++] = l[j++];
	 }
	
	return m;
}

	private int[] rangeOfArray(int[] arr ,int start,int end) {
		int[] a = new int[end-1];
		for(int i=start; i<end ;i++) {
			a[i] = arr[i];
		}
		return a;
		
	}
	


}
