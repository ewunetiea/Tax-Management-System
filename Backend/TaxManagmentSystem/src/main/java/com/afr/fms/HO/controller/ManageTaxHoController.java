package com.afr.fms.HO.controller;

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
import com.afr.fms.HO.service.ManageTaxHoService;
import com.afr.fms.Maker.entity.Tax;

@RestController
@RequestMapping("/api/ho/manage-tax")
public class ManageTaxHoController {
    @Autowired
    private ManageTaxHoService manageTaxService;

    private static final Logger logger = LoggerFactory.getLogger(ManageTaxHoController.class);

    @PostMapping("/approve")
    public ResponseEntity<Void> approveTaxes(@RequestBody List<Tax> taxes) {
        try {
            manageTaxService.approveTaxes(taxes);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
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
