package com.tms.Approver.Controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tms.Approver.Service.ManageTaxApproverService;
import com.tms.Maker.entity.Tax;

@RestController
@RequestMapping("/api/approver/manage-tax")
public class ManageTaxApproverController {
    @Autowired
    private ManageTaxApproverService manageTaxService;

    private static final Logger logger = LoggerFactory.getLogger(ManageTaxApproverController.class);

    @PostMapping("/approve")
    public ResponseEntity<Void> approveTaxes(@RequestBody List<Tax> taxes) {
        try {
            manageTaxService.approveTaxes(taxes);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
             logger.error("An error occurred while approving tax: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
