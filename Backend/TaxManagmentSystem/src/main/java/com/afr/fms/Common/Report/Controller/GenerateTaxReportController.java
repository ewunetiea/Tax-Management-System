package com.afr.fms.Common.Report.Controller;

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
import com.afr.fms.Common.Report.Service.GenerateTaxReportService;
import com.afr.fms.Maker.entity.Tax;
import com.afr.fms.Maker.entity.TaxableSearchEngine;

@RestController
@RequestMapping("/api/common/report")
public class GenerateTaxReportController {

    @Autowired
    private GenerateTaxReportService generateTaxReportService;

    private static final Logger logger = LoggerFactory.getLogger(GenerateTaxReportController.class);

    @PostMapping("/maker")
    public ResponseEntity<List<Tax>> generateReportForMaker(@RequestBody TaxableSearchEngine tax, HttpServletRequest request) {
        try {
            return new ResponseEntity<>(generateTaxReportService.generateReportForMaker(tax), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("An error occurred while searching taxes : {}", ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/reviewer")
    public ResponseEntity<List<Tax>> generateReportForReviewer(@RequestBody TaxableSearchEngine tax, HttpServletRequest request) {
        try {
            return new ResponseEntity<>(generateTaxReportService.generateReportForReviewer(tax), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("An error occurred while retrieving searching taxes : {}", ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/approver")
    public ResponseEntity<List<Tax>> generateReportForApprover(@RequestBody TaxableSearchEngine tax, HttpServletRequest request) {
        try {
            return new ResponseEntity<>(generateTaxReportService.generateReportForApprover(tax), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("An error occurred while retrieving searching taxes : {}", ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
