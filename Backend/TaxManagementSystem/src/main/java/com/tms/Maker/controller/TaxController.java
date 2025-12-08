package com.tms.Maker.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.tms.Maker.entity.MakerSearchPayload;
import com.tms.Maker.entity.Tax;
import com.tms.Maker.service.TaxableService;

import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/maker/tax")
public class TaxController {

	@Autowired
	private TaxableService taxableService;
	
	private static final Logger logger = LoggerFactory.getLogger(TaxController.class);

	@PostMapping("/fetchTaxBasedonStatus")
	public ResponseEntity<List<Tax>> fetchTaxBasedonStatus(@RequestBody MakerSearchPayload payload,
			HttpServletRequest request) {
		try {
			List<Tax> tax = new ArrayList<>();
			tax = taxableService.fetchTaxBasedonStatus(payload);

			return new ResponseEntity<>(tax, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching taxes", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/fetchTaxProgress")
	public ResponseEntity<List<Tax>> fetchTaxProgress(@RequestBody MakerSearchPayload payload, HttpServletRequest request) {
		try {
			List<Tax> tax = new ArrayList<>();
			tax = taxableService.fetchTaxProgress(payload);

			return new ResponseEntity<>(tax, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching taxes progress", ex);
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
			Tax savedTax = new Tax();

			if (tax.getId() != null) {

				savedTax = taxableService.updateTax(tax, files);

				return new ResponseEntity<>(savedTax, HttpStatus.OK);

			}

			else {

				savedTax = taxableService.createTaxWithFiles(tax, files);

				if (savedTax.getFileExsistance().contains("Exists")) {

					Map<String, String> response = new HashMap<>();
					response.put("message", "File already exists");
					// return new ResponseEntity<>(response, HttpStatus.CONFLICT);
					return new ResponseEntity<>(null, HttpStatus.CONFLICT);

				} else {

					return new ResponseEntity<>(savedTax, HttpStatus.OK);

				}

			}

		} catch (Exception ex) {
			logger.error("Error while saving tax or updating tax", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/delete")
	public ResponseEntity<Tax> deleteTax(@RequestBody List<Tax> taxs,
			HttpServletRequest request) {
		System.out.println(taxs);
		try {

			for (Tax acc : taxs) {
				taxableService.deleteTax(acc, taxs.get(0).getUser_id());
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {

			System.out.println(ex);
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
	public ResponseEntity<Tax> backTaxfromBranchManger(@RequestBody List<Tax> taxs,
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
