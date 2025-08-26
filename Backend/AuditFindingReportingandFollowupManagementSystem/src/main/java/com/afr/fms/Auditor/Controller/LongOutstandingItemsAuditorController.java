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
import com.afr.fms.Auditor.Service.LongOutstandingItemsAuditorService;
import com.afr.fms.Common.Entity.PaginatorPayLoad;

@RestController
@RequestMapping("/api/auditor/long_outstanding_items_audit")
public class LongOutstandingItemsAuditorController {

    @Autowired
    private LongOutstandingItemsAuditorService longOutstandingItemsAuditorService;

    private static final Logger logger = LoggerFactory.getLogger(LongOutstandingItemsAuditorController.class);

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createLongOutstandingItemsFinding(@RequestBody AuditISM audit,
            HttpServletRequest request) {
        try {

            if (audit.getId() != null) {
                longOutstandingItemsAuditorService.updateLongOutstandingItemsFinding(audit);
            } else {
                longOutstandingItemsAuditorService.createLongOutstandingItemsFinding(audit);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error creating/updating long outstanding items finding: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/drafted")
    public ResponseEntity<List<AuditISM>> getAuditsOnDrafting(@RequestBody PaginatorPayLoad paginatorPayLoad,
            HttpServletRequest request) {
        try {
            List<AuditISM> audits = longOutstandingItemsAuditorService.getAuditsOnDrafting(paginatorPayLoad);
            return new ResponseEntity<>(audits, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching drafted audits: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/passed")
    public ResponseEntity<List<AuditISM>> getPassedAuditFindings(@RequestBody PaginatorPayLoad paginatorPayLoad,
            HttpServletRequest request) {
        try {
            List<AuditISM> audits = longOutstandingItemsAuditorService.getPassedAuditFindings(paginatorPayLoad);
            return new ResponseEntity<>(audits, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching passed audit findings: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/rejected")
    public ResponseEntity<List<AuditISM>> geRejectedFindings(@RequestBody PaginatorPayLoad paginatorPayLoad,
            HttpServletRequest request) {

        try {
            List<AuditISM> audits = longOutstandingItemsAuditorService.geRejectedFindings(paginatorPayLoad);
            return new ResponseEntity<>(audits, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error fetching rejected findings: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
