package com.tms.Maker.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tms.Maker.entity.Tax;
import com.tms.Maker.entity.TaxableSearchEngine;
import com.tms.Maker.service.TaxableSearchEngineService;

@RestController
@RequestMapping("/api/search")
public class TaxableSearchEngineController {
    @Autowired
    private TaxableSearchEngineService taxableSearchEngineService;

    private static final Logger logger = LoggerFactory.getLogger(TaxableSearchEngineController.class);

   
    @PostMapping("/reviewer")
    public ResponseEntity<List<Tax>> getTaxableSearchEngineForReviewer(@RequestBody TaxableSearchEngine tax, HttpServletRequest request) {
        try {
            return new ResponseEntity<>(taxableSearchEngineService.getTaxableSearchEngineForReviewer(tax), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("An error occurred while retrieving searching taxes : {}", ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/approver")
    public ResponseEntity<List<Tax>> getTaxableSearchEngineForApprover(@RequestBody TaxableSearchEngine tax, HttpServletRequest request) {
        try {
            return new ResponseEntity<>(taxableSearchEngineService.getTaxableSearchEngineForApprover(tax), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("An error occurred while retrieving searching taxes : {}", ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
