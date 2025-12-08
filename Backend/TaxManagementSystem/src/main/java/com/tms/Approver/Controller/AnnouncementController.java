package com.tms.Approver.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.tms.Approver.Entity.Announcement;
import com.tms.Approver.Entity.AnnouncementPayload;
import com.tms.Approver.Service.AnnouncementService;
import com.tms.Common.Exception.DuplicateFileException;

@RestController
@RequestMapping("/api/approver/announcement")
public class AnnouncementController {

	@Autowired
	private AnnouncementService announcementService;

	private static final Logger logger = LoggerFactory.getLogger(AnnouncementController.class);

	// GET ongoing/archived
	@PostMapping("/fetch")
	public ResponseEntity<List<Announcement>> getAnnouncements(@RequestBody AnnouncementPayload payload) {
		try {
			if (payload.getAnnouncement_type().equals("ongoing"))
				return ResponseEntity.ok(announcementService.getOnGoingAnnouncements(payload.getRole()));

			if (payload.getAnnouncement_type().equals("archived"))
				return ResponseEntity.ok(announcementService.getArchivedAnnouncements(payload.getRole()));

			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			logger.error("Error fetching announcements", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// GET one
	@GetMapping("/{id}")
	public ResponseEntity<Announcement> getAnnouncementById(@PathVariable Long id) {
		return ResponseEntity.ok(announcementService.getAnnouncementById(id));
	}

	// CREATE or UPDATE
	@PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> createOrUpdateAnnouncement(@RequestPart("announcement") Announcement announcement, @RequestPart(value = "files", required = false) MultipartFile[] files) {
		try {
			// UPDATE
			if (announcement.getId() != null) {
				announcementService.updateAnnouncement(announcement, files);
				return ResponseEntity.ok(announcement);
			}

			// CREATE
			Announcement result = announcementService.createAnnouncement(announcement, files);
			return ResponseEntity.ok(result);

		} catch (DuplicateFileException ex) {
			logger.warn("Duplicate file detected: " + ex.getMessage());
			Map<String, String> response = new HashMap<>();
			response.put("error", "duplicate_file");
			response.put("message", ex.getMessage());

			return ResponseEntity.status(HttpStatus.CONFLICT).body(response); // 409
		} catch (Exception ex) {
			logger.error("Error creating/updating announcement", ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// DELETE
	@PostMapping("/delete")
	public ResponseEntity<?> deleteAnnouncements(@RequestBody List<Announcement> announcements) {
		try {
			for (Announcement a : announcements) {
				announcementService.deleteAnnouncement(a.getId());
			}
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			logger.error("Error deleting announcements", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// Dashboard
	@GetMapping("/fetch/dashboard/{type}")
	public ResponseEntity<Announcement> fetchDashboard(@PathVariable String type) {
		try {
			return ResponseEntity.ok(announcementService.getAnnouncementForDashBoard(type));
		} catch (Exception e) {
			logger.error("Error fetching dashboard", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
