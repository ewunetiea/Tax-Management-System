package com.afr.fms.Common.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.afr.fms.Admin.Entity.NotifyAdmin;
import com.afr.fms.Common.Permission.Service.FunctionalitiesService;
import com.afr.fms.Common.Service.NotificationService;

import com.afr.fms.Payload.realtime.RealTime;
import com.afr.fms.Security.UserSecurity.service.UserSecurityService;

@RestController
@RequestMapping("/api")
public class NotificationController {

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private UserSecurityService userSecurityService;

	@Autowired
	private FunctionalitiesService functionalitiesService;

	@PostMapping("/notifyAdmin")
	public ResponseEntity<List<NotifyAdmin>> notifyAdmin(@RequestBody RealTime realtime, HttpServletRequest request) {
		try {
			return new ResponseEntity<>(userSecurityService.notifyAdmin(realtime), HttpStatus.OK);
		} catch (Exception ex) {

			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/viewedNotificationsByAdmin")
	public ResponseEntity<HttpStatus> viewedNotificationsByAdmin(@RequestBody List<NotifyAdmin> adminNotification,
			HttpServletRequest request) {
		try {
			userSecurityService.viewedNotificationsByAdmin(adminNotification);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {

			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
