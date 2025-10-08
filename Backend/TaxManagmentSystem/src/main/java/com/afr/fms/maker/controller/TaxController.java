package com.afr.fms.maker.controller;



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
import com.afr.fms.maker.entity.Tax;
import com.afr.fms.maker.service.TaxableService;

@RestController
@RequestMapping("/api/tax")
public class TaxController {

	@Autowired
	private TaxableService taxableService;

	private static final Logger logger = LoggerFactory.getLogger(TaxController.class);

	@GetMapping("/fetch/{user_id}")
	public ResponseEntity<List<Tax>> getTax(HttpServletRequest request,
			@PathVariable("user_id") int user_id) {
		try {
			

			List<Tax> tax = new ArrayList<>();
			
			tax =	taxableService.fetchTax();

			
			
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

	@PostMapping("/create")
	public ResponseEntity<Tax> saveTax(@RequestBody Tax tax,
			HttpServletRequest request) {
		try {

			String mainGuid = "";
			if (tax.getMainGuid() == null) {
				mainGuid = taxableService.createTax(tax);
				tax.setMainGuid(mainGuid);

			} else {

				taxableService.updateTax(tax);

		

			}

			return new ResponseEntity<>(tax, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving account", ex);
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
