package com.afr.fms.Common.Finding;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/common")

public class FindingController {
	@Autowired
	private FindingService commonFindingService;

	private static final Logger logger = LoggerFactory.getLogger(FindingController.class);

	@GetMapping("/finding/{id}")
	public ResponseEntity<List<Finding>> getCommonFinding(@PathVariable Long id,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(commonFindingService.getCommonFinding(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("get_common_finding ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/findings/{identifier}")
	public ResponseEntity<List<Finding>> getCommonFindings(
			HttpServletRequest request, @PathVariable String identifier) {
		try {
			return new ResponseEntity<>(commonFindingService.getCommonFindings(identifier), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("get_common_findings ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/finding")
	public ResponseEntity<HttpStatus> addCommonFinding(@RequestBody Finding commonFinding,
			HttpServletRequest request) {
		try {
			if (commonFinding.getId() != null) {
				commonFindingService.updateCommonFinding(commonFinding);
			} else {
				commonFindingService.createCommonFinding(commonFinding);
			}

			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("add_common_finding ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping("/finding/{id}")
	public ResponseEntity<HttpStatus> deleteCommonFinding(@PathVariable Long id,
			HttpServletRequest request) {
		try {
			commonFindingService.deleteCommonFinding(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("delete_common_finding ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/delete/findings")
	public ResponseEntity<HttpStatus> deleteFindings(@RequestBody List<Finding> findings,
			HttpServletRequest request) {

		try {
			for (Finding commonFinding : findings) {
				commonFindingService.deleteCommonFinding(commonFinding.getId());
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("delete_common_findings ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
