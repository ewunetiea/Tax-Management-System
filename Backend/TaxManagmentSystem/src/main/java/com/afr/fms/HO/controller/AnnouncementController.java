package com.afr.fms.HO.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;
import com.afr.fms.HO.entity.Announcement;
import com.afr.fms.HO.service.AnnouncementService;
import com.afr.fms.maker.entity.Account;

@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {

	@Autowired
	private AnnouncementService announcementService;

	private static final Logger logger = LoggerFactory.getLogger(AnnouncementController.class);

	@GetMapping("/fetch/{announcemnetType}")
	public ResponseEntity<List<Announcement>> getAnnouncements(HttpServletRequest request,
			@PathVariable("announcemnetType") String announcemnetType) {
		try {
			

			System.out.println("____________________announcement type______________");
			System.out.print(announcemnetType);
			List<Announcement> announcements = new ArrayList<>();
			if (announcemnetType.contains("ongoing")) {
			announcements =	announcementService.getOnGoingAnnouncements();

			} else if (announcemnetType.contains("archived")) {
			announcements =	announcementService.getArchivedAnnouncements();


			System.out.println("array size is   "+ announcements.size());
			}
			return new ResponseEntity<>(announcements, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching on going announcements", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/{id}")
	public ResponseEntity<Announcement> getAnnouncementById(@PathVariable("id") Long id, HttpServletRequest request) {
		Announcement accounts = announcementService.getAnnouncementById(id);
		return new ResponseEntity<>(accounts, HttpStatus.OK);

	}

	@PostMapping("/create")
	public ResponseEntity<Announcement> saveAnnouncement(@RequestBody Announcement announcement,
			HttpServletRequest request) {
		try {

			Long Id = 0L;
			if (announcement.getId() == null) {
				Id = announcementService.createCreateAnnouncement(announcement);
				announcement.setId(Id);

			} else {

				announcementService.updateAnnouncement(announcement);

				Id = announcement.getId();

			}

			return new ResponseEntity<>(announcement, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving account", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/delete")
	public ResponseEntity<Account> deleteAnnouncements(@RequestBody List<Announcement> announcements,
			HttpServletRequest request) {
		try {

			for (Announcement acc : announcements) {
				announcementService.deleteAnnouncement(acc.getId());
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while deleting account", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
