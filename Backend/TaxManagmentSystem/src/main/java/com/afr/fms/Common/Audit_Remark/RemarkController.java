package com.afr.fms.Common.Audit_Remark;

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
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Auditor.Entity.AuditISM;

@RestController
@RequestMapping("/api/remark")
public class RemarkController {

    @Autowired
    RemarkService remarkService;

    private static final Logger logger = LoggerFactory.getLogger(RemarkController.class);

    @PostMapping("/getRemarks")
    private ResponseEntity<List<Remark>> getRemarks(@RequestBody Remark remark, HttpServletRequest request) {
        try {
            return new ResponseEntity<>(remarkService.getRemarks(remark), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error while fetching remark history: ", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/user")
    private ResponseEntity<List<User>> getUsersByCategory(@RequestBody AuditISM auditISM, HttpServletRequest request) {
        try {
            return new ResponseEntity<>(remarkService.getUserByCategory(auditISM), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error while fetching chat contacts", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/getUnseenRemarks")
    private ResponseEntity<List<Remark>> getUnseenRemarks(@RequestBody Remark remark, HttpServletRequest request) {
        try {
            return new ResponseEntity<>(remarkService.getUnseenRemarks(remark), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error while fetching unseen remarks: ", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/seenRemark")
    private ResponseEntity<HttpStatus> seenRemark(@RequestBody Remark remark, HttpServletRequest request) {
        try {
            remarkService.seenRemark(remark);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error while changing remark status: ", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/addRemark")
    private ResponseEntity<HttpStatus> addRemark(@RequestBody Remark remark, HttpServletRequest request) {
        try {
            remarkService.addRemark(remark);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error while adding rematk: ", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
