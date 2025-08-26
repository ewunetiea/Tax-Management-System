package com.afr.fms.Auditor.Controller;

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
import com.afr.fms.Auditor.Service.AbnormalBalanceAuditorService;
import com.afr.fms.Common.Entity.PaginatorPayLoad;

@RestController
@RequestMapping("/api/auditor/abnormal_balance_audit")
public class AbnormalBalanceAuditorController {
    @Autowired
    private AbnormalBalanceAuditorService abnormalBalanceAuditorService;

    private static final Logger logger = LoggerFactory.getLogger(AbnormalBalanceAuditorController.class);

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createISMFinding(@RequestBody AuditISM audit, HttpServletRequest request) {

        try {
            if (audit.getId() != null) {
                abnormalBalanceAuditorService.updateAbnormalBalanceinding(audit);
            } else {
                abnormalBalanceAuditorService.createAbnormalBalanceFinding(audit);
            }
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {

            logger.error("createISMFinding", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @PostMapping("/drafted")
    public ResponseEntity<List<AuditISM>> getAuditsOnDrafting(@RequestBody PaginatorPayLoad paginatorPayLoad,
            HttpServletRequest request) {
        try {

            List<AuditISM> audits = abnormalBalanceAuditorService.getAuditsOnDrafting(paginatorPayLoad);
            return new ResponseEntity<>(audits, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("getAuditsOnDrafting", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/passed")
    public ResponseEntity<List<AuditISM>> getAuditsOnProgress(@RequestBody PaginatorPayLoad paginatorPayLoad,
            HttpServletRequest request) {
        try {

            List<AuditISM> audits = abnormalBalanceAuditorService.getAuditsOnProgress(paginatorPayLoad);

            return new ResponseEntity<>(audits, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("getAuditsOnProgress", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/rejected")
    public ResponseEntity<List<AuditISM>> getRejectedFindings(@RequestBody PaginatorPayLoad paginatorPayLoad,
            HttpServletRequest request) {

        try {

            List<AuditISM> audits = abnormalBalanceAuditorService.getRejectedFindings(paginatorPayLoad);

            return new ResponseEntity<>(audits, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("getRejectedFindings", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
