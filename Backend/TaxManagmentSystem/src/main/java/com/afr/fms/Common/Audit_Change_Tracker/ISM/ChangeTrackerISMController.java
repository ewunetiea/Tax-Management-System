package com.afr.fms.Common.Audit_Change_Tracker.ISM;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afr.fms.Payload.endpoint.Endpoint;

@RestController
@RequestMapping("/api/ism")
public class ChangeTrackerISMController {

	@Autowired
	ChangeTrackerISMService changeTrackerISMService;

	@PostMapping("/change")
	public ResponseEntity<HttpStatus> insertChange(@RequestBody Change_Tracker_ISM change_Tracker_ISM,
			HttpServletRequest request) {
		try {
			changeTrackerISMService.insertChanges(change_Tracker_ISM);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/change/{audit_id}")
	public ResponseEntity<List<Change_Tracker_ISM>> getChanges(@PathVariable Long audit_id,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(changeTrackerISMService.getChanges(audit_id), HttpStatus.OK);
		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
