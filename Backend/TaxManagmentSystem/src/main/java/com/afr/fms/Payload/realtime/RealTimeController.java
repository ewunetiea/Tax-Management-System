package com.afr.fms.Payload.realtime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.afr.fms.Payload.endpoint.Endpoint;

@RestController
@RequestMapping("/api")
// @PreAuthorize("hasAnyRole('MAKER','APPROVER','HO','ADMIN')")
public class RealTimeController {

	@Autowired
	RealTimeService realTimeService;

	@PostMapping("/insertRealTimeInfo")
	public ResponseEntity<?> insertRealTimeInfo(@RequestBody RealTime realTime) {
		try {
			realTimeService.insertRealTimeInfo(realTime);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}


	@PostMapping("/getRealTimeByUserId")
	public ResponseEntity<RealTime> getRealTimeByUserId(@RequestBody RealTime realTime) {
		try {
			return new ResponseEntity<>(realTimeService.getRealTimeByUserId(realTime), HttpStatus.OK);
		} catch (Exception ex) {
			
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

}
