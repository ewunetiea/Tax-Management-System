package com.afr.fms.Common.SearchEngines;

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
import com.afr.fms.Auditor.Entity.AuditISM;


@RestController
@RequestMapping("/api/searchEngine")
public class SearchEngineController {

    @Autowired
    SearchEngineService searchEngineService; // SearchEngineService

    private static final Logger logger = LoggerFactory.getLogger(SearchEngineController.class);

    @PostMapping("/progress")
    private ResponseEntity<List<AuditISM>> getProgressFindings(@RequestBody AuditISM auditISM,
            HttpServletRequest request) {

        try {
            return new ResponseEntity<>(searchEngineService.getProgressFindings(auditISM), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error while fetching progress findings: {}", ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/scheduled")
    private ResponseEntity<List<AuditISM>> getScheduleRectificationFindings(@RequestBody AuditISM auditISM,
            HttpServletRequest request) {

        try {
            return new ResponseEntity<>(searchEngineService.getScheduleRectificationFindings(auditISM), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error while fetching schedule rectification findings: {}", ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/delegate")
    private ResponseEntity<HttpStatus> delegateUsers(@RequestBody List<AuditISM> auditISMs,
            HttpServletRequest request) {

        try {
            // if (functionalitiesService.verifyPermission(request, "delegate_users")) {
            searchEngineService.delegateUsers(auditISMs);
            return new ResponseEntity<>(HttpStatus.OK);
            // } else {
            // return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            // }
        } catch (Exception ex) {
            logger.error("Error while fetching progress findings: {}", ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/auditee")
    private ResponseEntity<HttpStatus> manageAuditees(@RequestBody AuditISM auditISM,
            HttpServletRequest request) {

        try {
            // if (functionalitiesService.verifyPermission(request, "delegate_users")) {
            searchEngineService.manageAuditees(auditISM);
            return new ResponseEntity<>(HttpStatus.OK);
            // } else {
            // return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            // }
        } catch (Exception ex) {
            logger.error("Error while managing auditees: {}", ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
