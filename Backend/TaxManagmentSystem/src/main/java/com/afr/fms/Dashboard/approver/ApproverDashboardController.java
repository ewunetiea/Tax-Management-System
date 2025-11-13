package com.afr.fms.Dashboard.approver;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/approver/dashboard")
public class ApproverDashboardController {

    @Autowired
    private ApproverDashboardService approverDashboardService;

    private static final Logger logger = LoggerFactory.getLogger(ApproverDashboardController.class);

    @GetMapping("/tax-status-card/{id}")
    public ResponseEntity<List<Integer>> getTaxStatusForApprover(@PathVariable("id") Long branch_id, HttpServletRequest request) {
        try {
            List<Integer> tax_status = approverDashboardService.getTaxStatusForApprover(branch_id);
            return new ResponseEntity<>(tax_status, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error occurred while fetching tax status for reviewer dash board: {}", ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tax-status-stacked-bar/{id}")
    public ResponseEntity<List<Map<String, Object>>> getStackedBarTaxesStatusData(@PathVariable("id") Long branch_id) {
        try {
            List<Map<String, Object>> taxStatusData = approverDashboardService.getStackedBarTaxesStatusData(branch_id);
            return new ResponseEntity<>(taxStatusData, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error occurred while fetching tax status for reviewer dashboard: {}", ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
