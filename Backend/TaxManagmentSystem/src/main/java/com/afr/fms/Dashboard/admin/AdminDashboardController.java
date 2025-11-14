package com.afr.fms.Dashboard.admin;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.afr.fms.Common.RecentActivity.RecentActivity;

@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {

    @Autowired
    private AdminDashboardService dashboardService;

    // ===== Get Card Data =====
    @GetMapping("/card-data")
    public ResponseEntity<List<Integer>> getCardData(HttpServletRequest request) {
        try {
            List<Integer> cardData = dashboardService.computeCardData();
            return new ResponseEntity<>(cardData, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ===== Get Polar Data =====
    @GetMapping("/dougnut")
    public ResponseEntity<Map<String, Integer>> getPolarData(@RequestParam Long userId, HttpServletRequest reques) {
        try {
           // Updated to not require userId
           

           
            return new ResponseEntity<>(dashboardService.getDougnutData(), HttpStatus.OK);
        } catch (Exception ex) {
            
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ===== Get Bar Chart Data =====
    @GetMapping("/branch-per-region")
    public ResponseEntity<Map<String, List<?>>> getBarChartData(HttpServletRequest request) {
        try {
           
            return new ResponseEntity<>(dashboardService.getRegionBranchDashboardData(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    

    // ===== Get Recent Activity =====
    @GetMapping("/recent-activity/{userId}")
    public ResponseEntity<List<RecentActivity>> getRecentActivity(@PathVariable Long userId,HttpServletRequest reques) {
        try {
            List<RecentActivity> recentActivity = dashboardService.getRecentActivity(userId);
            return new ResponseEntity<>(recentActivity, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}