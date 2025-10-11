package com.afr.fms.Common.FeesChargeCommission.Controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.afr.fms.Common.FeesChargeCommission.Model.FeeChargeCommissionCategory;
import com.afr.fms.Common.FeesChargeCommission.Service.FeeChargeCommissionCategoryService;
import com.afr.fms.Payload.endpoint.Endpoint;

@RestController
@RequestMapping("/api/fee_charge_commission")
public class FeeChargeCommissionCategoryController {
	@Autowired
	private FeeChargeCommissionCategoryService feeChargeCommissionCategoryService;


	@GetMapping("/feeCommissionCategory/{id}")
	public ResponseEntity<FeeChargeCommissionCategory> getFeeChargeCommissionCategoryById(@PathVariable Long id,
			HttpServletRequest request) {
		// if (functionalitiesService.verifyPermission(request, "get_Common_Finding")) {
		try {
			return new ResponseEntity<>(feeChargeCommissionCategoryService.getFeeChargeCommissionCategoryById(id), HttpStatus.OK);
		} catch (Exception ex) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// } else {
		// return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		// }
	}


	@GetMapping("/feeCommissionCategory")
	public ResponseEntity<List<FeeChargeCommissionCategory>> feeCommissionCategory(
			HttpServletRequest request) {
		// if (functionalitiesService.verifyPermission(request, "get_Common_Finding")) {
		try {
			return new ResponseEntity<>(feeChargeCommissionCategoryService.getFeeChargeCommissionCategory(), HttpStatus.OK);
		} catch (Exception ex) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// } else {
		// return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		// }
	}
	

	@PostMapping("/feeCommissionCategory-create")
	public ResponseEntity<HttpStatus> addFeeCommissionCategory(@RequestBody FeeChargeCommissionCategory feeCommissionCategory,
			HttpServletRequest request) {
		// if (functionalitiesService.verifyPermission(request, "add_Common_Finding")) {
		try {
			if (feeCommissionCategory.getId() != null) {
				feeChargeCommissionCategoryService.updateFeeChargeCommissionCategory(feeCommissionCategory);
			} else {
				feeChargeCommissionCategoryService.createFeeChrgeCommisionCategory(feeCommissionCategory);
			}

			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// } else {
		// return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		// }

	}

	@DeleteMapping("/feeCommissionCategorybyId/{id}")
	public ResponseEntity<HttpStatus> deleteFeeCommissionCategory(@PathVariable Long id,
			HttpServletRequest request) {
		// if (functionalitiesService.verifyPermission(request,
		// "delete_Common_Finding")) {
		try {
			feeChargeCommissionCategoryService.deleteFeeChargeCommissionCategory(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// } else {
		// return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		// }
	}

	@PostMapping("/delete/feeCommissionCategory")
	public ResponseEntity<HttpStatus> deleteFindings(@RequestBody List<FeeChargeCommissionCategory> findings,
			HttpServletRequest request) {
		// if (functionalitiesService.verifyPermission(request,
		// "delete_Common_Finding")) {
		try {
			for (FeeChargeCommissionCategory commonFinding : findings) {
				feeChargeCommissionCategoryService.deleteFeeChargeCommissionCategory(commonFinding.getId());
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// } else {
		// return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		// }
	}
}
