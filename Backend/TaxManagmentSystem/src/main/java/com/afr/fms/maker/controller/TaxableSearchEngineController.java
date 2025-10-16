package com.afr.fms.Maker.controller;

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
import com.afr.fms.Maker.entity.Tax;
import com.afr.fms.Maker.entity.TaxableSearchEngine;
import com.afr.fms.Maker.service.TaxableSearchEngineService;

@RestController
@RequestMapping("/api/maker")
public class TaxableSearchEngineController {
    @Autowired
    private TaxableSearchEngineService taxableSearchEngineService;

    private static final Logger logger = LoggerFactory.getLogger(TaxableSearchEngineController.class);

    @PostMapping("/maker/search")
    public ResponseEntity<List<Tax>> getTaxableSearchEngineForMaker(@RequestBody TaxableSearchEngine tax, HttpServletRequest request) {
        try {
            return new ResponseEntity<>(taxableSearchEngineService.getTaxableSearchEngineForMaker(tax), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("An error occurred while searching taxes : {}", ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/reviewer/search")
    public ResponseEntity<List<Tax>> getTaxableSearchEngineForReviewer(@RequestBody TaxableSearchEngine tax, HttpServletRequest request) {
        try {
            return new ResponseEntity<>(taxableSearchEngineService.getTaxableSearchEngineForReviewer(tax), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("An error occurred while retrieving searching taxes : {}", ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/approver/search")
    public ResponseEntity<List<Tax>> getTaxableSearchEngineForApprover(@RequestBody TaxableSearchEngine tax, HttpServletRequest request) {
        try {
            return new ResponseEntity<>(taxableSearchEngineService.getTaxableSearchEngineForApprover(tax), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("An error occurred while retrieving searching taxes : {}", ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
