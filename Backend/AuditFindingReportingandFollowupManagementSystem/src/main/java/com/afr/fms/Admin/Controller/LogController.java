package com.afr.fms.Admin.Controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.afr.fms.Admin.Entity.Log;
import com.afr.fms.Admin.Service.LogService;

import com.afr.fms.Common.Permission.Service.FunctionalitiesService;

@RestController
@RequestMapping("/api")
public class LogController {

    @Autowired
    private FunctionalitiesService functionalitiesService;

    @Autowired
    private LogService logService;

    @GetMapping("/log")
    public ResponseEntity<List<Log>> getLogRecords(HttpServletRequest request) {
        try {
            return new ResponseEntity<>(logService.getLogRecords(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/log")
    public ResponseEntity<?> deleteLogs(@RequestBody List<Log> logs, HttpServletRequest request) {
        try {
            for (Log log : logs) {
                logService.deleteLogRecordById(log.getId());
            }
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception ex) {

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        }
    }

}
