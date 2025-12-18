package com.tms.Admin.Controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tms.Admin.Entity.AdminReport;
import com.tms.Admin.Entity.UserTracker;
import com.tms.Admin.Mapper.UserTrackerMapper;
import com.tms.Admin.Service.RegionService;
import com.tms.Admin.Service.ReportService;
import com.tms.Common.Entity.PaginatorPayLoad;
import com.tms.Security.UserSecurity.service.RefreshTokenService;


@RestController
@RequestMapping("/api")
public class ReportController {
    @Autowired
    private RegionService regionService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserTrackerMapper userTrakerMapper;

    @GetMapping("/region/branch_per_region")
    public ResponseEntity<List<Object>> drawBranchPerRegionLineChart() {
        try {
            return new ResponseEntity<>(regionService.drawBranchPerRegionLineChart(), HttpStatus.OK);
        } catch (Exception ex) {
            
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/user_per_region")
    public ResponseEntity<List<AdminReport>> drawBarChartUsersPerRegion() {
        try {
            return new ResponseEntity<>(reportService.drawBarChartUsersPerRegion(), HttpStatus.OK);
        } catch (Exception ex) {
            
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/loginStatus")
    public ResponseEntity<List<UserTracker>> getOnlineOfflineUsers(HttpServletRequest request, @RequestBody PaginatorPayLoad paginatorPayLoad) {
        try {
            // refreshTokenService.verifyOnlineUsers();
            List<UserTracker> userTracker = reportService.getOnlineFailedUsers(paginatorPayLoad);
            return new ResponseEntity<>(userTracker, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/login_status")
    public ResponseEntity<HttpStatus> updateLoginStatus(HttpServletRequest request,
            @RequestBody List<UserTracker> userTracker) {

        try {
            reportService.updateLoginStatus(userTracker);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
