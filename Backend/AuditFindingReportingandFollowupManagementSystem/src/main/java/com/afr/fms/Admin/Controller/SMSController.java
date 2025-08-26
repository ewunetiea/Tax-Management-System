package com.afr.fms.Admin.Controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.afr.fms.Admin.Entity.Branch;
import com.afr.fms.Admin.Entity.SMS;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Service.SMSService;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;


@RestController
@RequestMapping("/api/admin/SMS/")
public class SMSController {

	@Autowired
	private SMSService smsService;
	@Autowired
	private RecentActivityMapper recentActivityMapper;

	RecentActivity recentActivity = new RecentActivity();

	@GetMapping("/fetch")
	public ResponseEntity<List<SMS>> getSMS() {

		try {
			List<SMS> sms = smsService.getSMS();
			return new ResponseEntity<>(sms, HttpStatus.OK);
		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/active")
	public ResponseEntity<List<SMS>> getActiveSMS() {

		try {
			List<SMS> sms = smsService.getActiveSMS();
			return new ResponseEntity<>(sms, HttpStatus.OK);
		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/sms")
	public ResponseEntity<HttpStatus> manageSMS(@RequestBody SMS sms, HttpServletRequest request) {
		try {
			User user = new User();
			if (sms.getId() == null) {
				smsService.createSMS(sms);
				recentActivity.setMessage(sms.getApi() + " SMS API is created ");
				user.setId(sms.getUser_id());
				recentActivity.setUser(user);
				recentActivityMapper.addRecentActivity(recentActivity);
			} else {
				smsService.updateSMS(sms);
				recentActivity.setMessage(sms.getApi() + " SMS API info is updated ");
				user.setId(sms.getUser_id());
				recentActivity.setUser(user);
				recentActivityMapper.addRecentActivity(recentActivity);
			}

			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/status")
	public ResponseEntity<Branch> manageSMSStatus(@RequestBody List<SMS> sms) {

		for (SMS sms1 : sms) {
			smsService.manageSMSStatus(sms1);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
