package com.afr.fms.Auditor.Controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Service.CreditDocumentationService;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import com.afr.fms.Model.MGT.CreditDocumentationChild;
import com.afr.fms.Model.MGT.CreditDocumentationParent;

@RestController
@RequestMapping("/api/creditDoc")
public class CreditDocumentationAuditorController {
    @Autowired
    private CreditDocumentationService creditDocumentationNewService;

    @PostMapping("/create")
    public ResponseEntity<Long> createLoanAndAdvance(@RequestBody AuditISM audit, HttpServletRequest request) {

        Long loanId = 0L; // Proper variable naming
        if (audit.getId() != null) {
            creditDocumentationNewService.updateAmendedLoanAndAdvanceParent(audit);
            return ResponseEntity.ok(audit.getCreditDocumentationParent().getId()); // Return 0 as loanId remains
                                                                                    // unchanged
        } else {
            loanId = creditDocumentationNewService.createCreditDocumentationParent(audit);
            return ResponseEntity.ok(loanId); // Return 0 as loanId remains unchanged
        }

    }

    @PostMapping("/getFindingBasedOnStatus")
    public ResponseEntity<List<AuditISM>> getFindingBasedOnStatus(@RequestBody PaginatorPayLoad paginatorPayLoad,
            HttpServletRequest request) {

        return new ResponseEntity<>(creditDocumentationNewService.getFindingBasedOnStatus(paginatorPayLoad),
                HttpStatus.OK);

    }

    @PostMapping("/create/child")
    public ResponseEntity<?> createLoanAndAdvanceCommon(@RequestBody CreditDocumentationChild updated,
            HttpServletRequest request) {

        if (updated.getId() != null) {
            creditDocumentationNewService.updateDynamicLoanAdvance(updated);
        } else {
            creditDocumentationNewService.createCreditDocumentationChild(updated);
        }
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @GetMapping("/check_existance/{account_number}")
    public ResponseEntity<Boolean> checkAccountNumberExist(@PathVariable String account_number) {
        return new ResponseEntity<>(creditDocumentationNewService.checkAccountNumberExist(account_number),
                HttpStatus.OK);
    }

    @GetMapping("/creditDocumentationByAuditId/{audit_id}")
    public ResponseEntity<CreditDocumentationParent> getAmendedLoanAndAdvaceByAuditId(@PathVariable Long audit_id,
            HttpServletRequest request) {

        return new ResponseEntity<>(creditDocumentationNewService.getAmendedLoanAndAdvanceById(audit_id),
                HttpStatus.OK);

    }

}
