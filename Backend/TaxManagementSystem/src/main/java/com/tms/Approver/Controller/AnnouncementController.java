package com.tms.Approver.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tms.Approver.Entity.Announcement;
import com.tms.Approver.Entity.AnnouncementPayload;
import com.tms.Approver.Service.AnnouncementService;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/approver/announcement")
public class AnnouncementController {

	@Autowired
	private AnnouncementService announcementService;

	private static final Logger logger = LoggerFactory.getLogger(AnnouncementController.class);

	@PostMapping("/fetch")
	public ResponseEntity<List<Announcement>> getAnnouncements( @RequestBody AnnouncementPayload announcementPayload ,  HttpServletRequest request) {
		try {
			List<Announcement> announcements = new ArrayList<>();
			if (announcementPayload.getAnnouncement_type().contains("ongoing")) {
				announcements = announcementService.getOnGoingAnnouncements(announcementPayload.getRole());
			} else if (announcementPayload.getAnnouncement_type().contains("archived")) {
				announcements = announcementService.getArchivedAnnouncements(announcementPayload.getRole());
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

	// @PostMapping("/create")
	// public ResponseEntity<Announcement> createAnnouncement(@RequestBody Announcement announcement, HttpServletRequest request) {
	// 	try {
	// 		Long Id = 0L;
	// 		if (announcement.getId() == null) {
	// 			Id = announcementService.createCreateAnnouncement(announcement);
	// 			announcement.setId(Id);
	// 		} else {
	// 			announcementService.updateAnnouncement(announcement);
	// 			Id = announcement.getId();
	// 		}
	// 		return new ResponseEntity<>(announcement, HttpStatus.OK);
	// 	} catch (Exception ex) {
	// 		logger.error("Error while saving account", ex);
	// 		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	// 	}
	// }

	@PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Announcement> createAnnouncement(@RequestPart("announcement") Announcement announcement, @RequestPart(value = "files", required = false) MultipartFile[] files) {
		try {
			Announcement savedAnnouncement = new Announcement();
			if (announcement.getId() != null) {
				announcementService.updateAnnouncement(announcement, files);
				return new ResponseEntity<>(announcement, HttpStatus.OK);
			}

			else {
				announcement = announcementService.createCreateAnnouncement(announcement, files);
				if (announcement.getFileExsistance().contains("Exists")) {
					Map<String, String> response = new HashMap<>();
					response.put("message", "File already exists");
					// return new ResponseEntity<>(response, HttpStatus.CONFLICT);
					return new ResponseEntity<>(null, HttpStatus.CONFLICT);
				} else {
					announcement.setId(savedAnnouncement.getId());
					announcement.setMainGuid(savedAnnouncement.getMainGuid());
					return new ResponseEntity<>(announcement, HttpStatus.OK);
				}
			}

		} catch (Exception ex) {
			logger.error("Error while saving tax or updating tax", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/delete")
	public ResponseEntity<Announcement> deleteAnnouncements(@RequestBody List<Announcement> announcements, HttpServletRequest request) {
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

	@GetMapping("/fetch/dashboard/{type}")
	public ResponseEntity<Announcement> getAnnouncementForDashbpard(@PathVariable("type") String type, HttpServletRequest request) {
		try {
			Announcement announcement = new Announcement();
			announcement = announcementService.getAnnouncementForDashBoard(type);
			return new ResponseEntity<>(announcement, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching on going announcements", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	

}
