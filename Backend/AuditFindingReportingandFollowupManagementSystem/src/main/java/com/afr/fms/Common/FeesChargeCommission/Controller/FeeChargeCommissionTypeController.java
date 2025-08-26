package com.afr.fms.Common.FeesChargeCommission.Controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afr.fms.Common.FeesChargeCommission.Model.FeeChargeCommissionType;
import com.afr.fms.Common.FeesChargeCommission.Service.FeeChargeCommissionTypeService;
import com.afr.fms.Payload.endpoint.Endpoint;


@RestController
@RequestMapping("/api/fee_charge_commission")

public class FeeChargeCommissionTypeController {
	@Autowired
	private FeeChargeCommissionTypeService feeChargeCommissionTypeService;

	@GetMapping("/feeCommissionType/{id}")
	public ResponseEntity<FeeChargeCommissionType> feeCommissionType(@PathVariable Long id,
			HttpServletRequest request) {
		// if (functionalitiesService.verifyPermission(request, "get_Common_Finding")) {
		try {
			return new ResponseEntity<>(feeChargeCommissionTypeService.getFeeChargeCommissionCTypeById(id),
					HttpStatus.OK);
		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// } else {
		// return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		// }
	}

	@GetMapping("/feeCommissionType")
	public ResponseEntity<List<FeeChargeCommissionType>> FeeChargeCommissionType(
			HttpServletRequest request) {
		// if (functionalitiesService.verifyPermission(request, "get_Common_Finding")) {
		try {
			return new ResponseEntity<>(feeChargeCommissionTypeService.FeeChargeCommissionType(), HttpStatus.OK);
		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// } else {
		// return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		// }
	}

	@PostMapping("/feeCommissionType-create")
	public ResponseEntity<HttpStatus> addFeeCommissionType(@RequestBody FeeChargeCommissionType feeCommissionType,
			HttpServletRequest request) {
		// if (functionalitiesService.verifyPermission(request, "add_Common_Finding")) {
		try {
			if (feeCommissionType.getId() != null) {
				feeChargeCommissionTypeService.updateFeeChargeCommissionType(feeCommissionType);
			} else {
				feeChargeCommissionTypeService.createCommonfeeChargeCommissionType(feeCommissionType);
			}

			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// } else {
		// return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		// }

	}

	@DeleteMapping("/feeCommissionTypeById/{id}")
	public ResponseEntity<HttpStatus> deleteFeeCommissionType(@PathVariable Long id,
			HttpServletRequest request) {
		// if (functionalitiesService.verifyPermission(request,
		// "delete_Common_Finding")) {
		try {
			feeChargeCommissionTypeService.deleteFeeChargeCommissionType(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// } else {
		// return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		// }
	}

	@PostMapping("/delete/feeCommissionType")
	public ResponseEntity<HttpStatus> deleteFindings(@RequestBody List<FeeChargeCommissionType> feeCommissionTypes,
			HttpServletRequest request) {
		// if (functionalitiesService.verifyPermission(request,
		// "delete_Common_Finding")) {
		try {

			for (FeeChargeCommissionType feeChargeCommissionType : feeCommissionTypes) {
				feeChargeCommissionTypeService.deleteFeeChargeCommissionType(feeChargeCommissionType.getId());
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// } else {
		// return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		// }
	}

}
