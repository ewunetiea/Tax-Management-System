package com.afr.fms.Admin.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.afr.fms.Admin.Entity.Backup;
import com.afr.fms.Admin.Service.BackupService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
@RequestMapping("/api/backup")
public class BackupController {

	@Autowired
	private BackupService backupService;

	@PostMapping("/createBackup")
	public ResponseEntity<?> createBackup(@RequestBody Backup backup, HttpServletRequest request) {
		try {
			backupService.createBackup(backup);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(value = "/getBackup")
	private ResponseEntity<Object> getBackup(@RequestBody Backup backup, HttpServletRequest request)
			throws JsonMappingException, JsonProcessingException {

		Backup successfulbackup = backupService.createBackup(backup);
		File file = new File(successfulbackup.getFilepath());
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
			headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Pragma", "no-cache");
			headers.add("Expires", "0");
			String contentType = "application/octet-stream";
			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

			return ResponseEntity.ok()
					.headers(headers)
					.contentLength(file.length())
					.contentType(
							MediaType.parseMediaType(contentType))
					.body(resource);
		}

		catch (IOException e) {
			return ResponseEntity.badRequest()
					.body("Couldn't find " + file.getName() +
							" => " + e.getMessage());
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (file.delete()) {
				System.out.println("File deleted");
			} else {
				System.out.println("File not deleted!");
			}
			;
		}

	}

	@GetMapping("/getBackupByUserId/{id}")
	public ResponseEntity<Backup> getBackupByUserId(@PathVariable("id") Long id, HttpServletRequest request) {

		try {
			return new ResponseEntity<>(backupService.getBackupByUserId(id), HttpStatus.OK);
		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.OK);
		}

	}
}
