package com.afr.fms.Auditor.Controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Service.GeneeralObservationAuditorService;
import com.afr.fms.Common.Entity.PaginatorPayLoad;

@RestController
@RequestMapping("/api/auditor/general_observation_audit")
public class GeneeralObservationAuditorController {
    @Autowired
    private GeneeralObservationAuditorService geneeralObservationAuditorService;

    private static final Logger logger = LoggerFactory.getLogger(GeneeralObservationAuditorController.class);

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createISMFinding(@RequestBody AuditISM audit, HttpServletRequest request) {
        try {
            if (audit.getId() != null) {
                geneeralObservationAuditorService.updateGeneralObservationFinding(audit);
            } else {
                geneeralObservationAuditorService.createGeneralObservationFinding(audit);
            }
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error creating/updating general observation finding: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/drafted")
    public ResponseEntity<List<AuditISM>> getAuditsOnDrafting(@RequestBody PaginatorPayLoad paginatorPayLoad,
            HttpServletRequest request) {

        try {
            List<AuditISM> audits = geneeralObservationAuditorService.getAuditsOnDrafting(paginatorPayLoad);
            return new ResponseEntity<>(audits, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching drafted audits: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/passed")
    public ResponseEntity<List<AuditISM>> getAuditsOnProgress(@RequestBody PaginatorPayLoad paginatorPayLoad,
            HttpServletRequest request) {
        try {
            List<AuditISM> audits = geneeralObservationAuditorService.getAuditsOnProgress(paginatorPayLoad);
            return new ResponseEntity<>(audits, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching audits in progress: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/rejected")
    public ResponseEntity<List<AuditISM>> getRejectedFindings(@RequestBody PaginatorPayLoad paginatorPayLoad,
            HttpServletRequest request) {
        try {
            List<AuditISM> audits = geneeralObservationAuditorService.getRejectedFindings(paginatorPayLoad);
            return new ResponseEntity<>(audits, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching rejected findings: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
