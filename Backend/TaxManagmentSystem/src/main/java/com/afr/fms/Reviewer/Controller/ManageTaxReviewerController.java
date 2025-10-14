package com.afr.fms.Reviewer.Controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import com.afr.fms.Maker.entity.Tax;
import com.afr.fms.Reviewer.Service.ManageTaxReviewerService;

@RestController
@RequestMapping("/api/reviewer/manage-tax")
public class ManageTaxReviewerController {
    @Autowired
    private ManageTaxReviewerService manageTaxService;

    private static final Logger logger = LoggerFactory.getLogger(ManageTaxReviewerController.class);

    @PostMapping("/pending")
    public ResponseEntity<List<Tax>> getPendingTaxes(@RequestBody PaginatorPayLoad paginatorPayLoad,
            HttpServletRequest request) {
        try {
            return new ResponseEntity<>(manageTaxService.getPendingTaxes(paginatorPayLoad), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("An error occurred while retrieving pending taxes : {}", ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/rejected")
    public ResponseEntity<List<Tax>> getRejectedTaxes(@RequestBody PaginatorPayLoad paginatorPayLoad,
            HttpServletRequest request) {
        try {
            return new ResponseEntity<>(manageTaxService.getRejectedTaxes(paginatorPayLoad), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("An error occurred while retrieving rejected taxes : {}", ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/approved")
    public ResponseEntity<List<Tax>> getApprovedTaxes(@RequestBody PaginatorPayLoad paginatorPayLoad,
            HttpServletRequest request) {
        try {
            return new ResponseEntity<>(manageTaxService.getApprovedTaxes(paginatorPayLoad), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("An error occurred while retrieving approved taxes : {}", ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/review")
    public ResponseEntity<Void> reviewTaxes(@RequestBody List<Tax> taxes) {
        manageTaxService.reviewTaxes(taxes);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reject")
    public ResponseEntity<Void> rejectTax(@RequestBody Tax tax) {
        try {
            manageTaxService.rejectTax(tax);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            logger.error("An error occurred while rejecting tax: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
