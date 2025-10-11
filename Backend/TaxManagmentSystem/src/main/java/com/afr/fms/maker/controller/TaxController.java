package com.afr.fms.Maker.controller;

import java.io.File;
import java.nio.file.Paths;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.afr.fms.Maker.entity.Tax;
import com.afr.fms.Maker.service.TaxableService;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/tax")
public class TaxController {

	@Autowired
	private TaxableService taxableService;

	private static final Logger logger = LoggerFactory.getLogger(TaxController.class);

	@GetMapping("/fetch/{maker_name}")
	public ResponseEntity<List<Tax>> getTax(HttpServletRequest request,
			@PathVariable("maker_name") String maker_name) {
		try {

			List<Tax> tax = new ArrayList<>();

			tax = taxableService.fetchTax(maker_name);

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

	// @PostMapping("/create")
	// public ResponseEntity<Tax> saveTax(@RequestBody Tax tax,
	// HttpServletRequest request) {
	// try {

	// String mainGuid = "";
	// if (tax.getMainGuid() == null) {
	// mainGuid = taxableService.createTax(tax);
	// tax.setMainGuid(mainGuid);

	// } else {

	// taxableService.updateTax(tax);

	// }

	// return new ResponseEntity<>(tax, HttpStatus.OK);
	// } catch (Exception ex) {
	// logger.error("Error while saving account", ex);
	// return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	// }

	// }

	// @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	// public ResponseEntity<Tax> saveTax(
	// 		@RequestPart("tax") Tax tax,
	// 		@RequestPart(value = "files", required = false) MultipartFile[] files,
	// 		HttpServletRequest request) {
	// 	try {
	// 		// Relative folder directly inside project root
	// 		String uploadDir = Paths.get(System.getProperty("user.dir"), "taxFiles").toString();

	// 		// Create folder if it doesn't exist
	// 		File dir = new File(uploadDir);
	// 		if (!dir.exists()) {
	// 			dir.mkdirs(); // creates the taxFiles folder
	// 		}

	// 		// Save uploaded files
	// 		if (files != null && files.length > 0) {
	// 			for (MultipartFile file : files) {
	// 				if (!file.isEmpty()) {
	// 					File destination = new File(dir, file.getOriginalFilename());
	// 					file.transferTo(destination);
	// 					
	// 				}
	// 			}
	// 		}

	// 		// Handle tax creation/updation
	// 		String mainGuid = "";
	// 		if (tax.getMainGuid() == null) {
	// 			mainGuid = taxableService.createTax(tax);
	// 			tax.setMainGuid(mainGuid);
	// 		} else {
	// 			taxableService.updateTax(tax);
	// 		}

	// 		return new ResponseEntity<>(tax, HttpStatus.OK);
	// 	} catch (Exception ex) {
	// 		logger.error("Error while saving tax", ex);
	// 		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	// 	}
	// }


	@PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<Tax> saveTax(
        @RequestPart("tax") Tax tax,
        @RequestPart(value = "files", required = false) MultipartFile[] files
) {
    try {
        String mainGuid = taxableService.createTaxWithFiles(tax, files);
        tax.setMainGuid(mainGuid);
        return new ResponseEntity<>(tax, HttpStatus.OK);
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
				taxableService.deleteTax(acc.getMainGuid());
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while deleting account", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	

}
