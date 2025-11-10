package com.afr.fms.Maker.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.afr.fms.Maker.entity.Tax;
import com.afr.fms.Maker.mapper.TaxFileMapper;
import com.afr.fms.Maker.service.TaxableService;
import com.afr.fms.Payload.payload.Payload;

import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/tax")
public class TaxController {

	@Autowired
	private TaxableService taxableService;

	@Autowired

	private TaxFileMapper taxFileMapper;

	private static final Logger logger = LoggerFactory.getLogger(TaxController.class);

	@PostMapping("/fetchTaxBasedonStatus")
	public ResponseEntity<List<Tax>> fetchTaxBasedonStatus(@RequestBody Payload payload, HttpServletRequest request) {
		try {

			List<Tax> tax = new ArrayList<>();

			tax = taxableService.fetchTaxBasedonStatus(payload);

			return new ResponseEntity<>(tax, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching on going announcements", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/{id}")
	public ResponseEntity<Tax> fetchTaxById(@PathVariable("id") int id, HttpServletRequest request) {
		Tax accounts = taxableService.fetchTaxById(id);
		return new ResponseEntity<>(accounts, HttpStatus.OK);

	}

	@PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Tax> saveTax(
			@RequestPart("tax") Tax tax,
			@RequestPart(value = "files", required = false) MultipartFile[] files) {
		try {

			String mainGuid = "";
			if (tax.getId() != null) {

				taxableService.updateTax(tax, files);

				mainGuid = tax.getMainGuid();

			}

			else {

				mainGuid = taxableService.createTaxWithFiles(tax, files);

			}

			if (mainGuid.contains("Exists")) {

				Map<String, String> response = new HashMap<>();
				response.put("message", "File already exists");
				// return new ResponseEntity<>(response, HttpStatus.CONFLICT);
				return new ResponseEntity<>(null, HttpStatus.CONFLICT);

			} else {

				tax.setMainGuid(mainGuid);

				return new ResponseEntity<>(tax, HttpStatus.OK);

			}

		} catch (Exception ex) {
			logger.error("Error while saving tax", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/delete")
	public ResponseEntity<Tax> deleteTax(@RequestBody List<Tax> taxs,
			HttpServletRequest request) {
		try {

			for (Tax acc : taxs) {
				taxableService.deleteTax(acc.getId());
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while deleting account", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/submit")
	public ResponseEntity<?> submitTaxToBranchManger(@RequestBody List<Tax> taxs,
			HttpServletRequest request) {
		try {

			for (Tax acc : taxs) {
				taxableService.submitTaxToBranchManger(acc.getId());
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while deleting account", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/back")
	public ResponseEntity<Tax> backTaxToBranchManger(@RequestBody List<Tax> taxs,
			HttpServletRequest request) {
		try {

			for (Tax acc : taxs) {
				taxableService.backTaxToDraftedState(acc.getId());
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while deleting account", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
