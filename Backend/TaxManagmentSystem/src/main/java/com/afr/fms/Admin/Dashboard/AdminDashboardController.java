package com.afr.fms.Admin.Dashboard;

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
    @GetMapping("/polar-data")
    public ResponseEntity<List<Integer>> getPolarData(@RequestParam Long userId, HttpServletRequest reques) {
        try {
            List<Integer> polarData = dashboardService.computePolarData(); // Updated to not require userId
            return new ResponseEntity<>(polarData, HttpStatus.OK);
        } catch (Exception ex) {
            
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ===== Get Bar Chart Data =====
    @GetMapping("/bar-chart")
    public ResponseEntity<List<Integer>> getBarChartData(HttpServletRequest request) {
        try {
            List<Integer> barChartData = dashboardService.computeBarChartData();
            return new ResponseEntity<>(barChartData, HttpStatus.OK);
        } catch (Exception ex) {
            
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ===== Get Horizontal Bar Chart Data =====
    @GetMapping("/horizontal-bar-chart")
    public ResponseEntity<List<Integer>> getHorizontalBarChartData(HttpServletRequest request) {
        try {
            List<Integer> horizontalBarChartData = dashboardService.computeHorizontalBarChartData();
            return new ResponseEntity<>(horizontalBarChartData, HttpStatus.OK);
        } catch (Exception ex) {
            
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ===== Get Radar Age Data =====
    @GetMapping("/radar-age")
    public ResponseEntity<List<Integer>> getRadarAgeData(HttpServletRequest reques) {
        try {
            List<Integer> radarAgeData = dashboardService.computeRadarAgeData();
            return new ResponseEntity<>(radarAgeData, HttpStatus.OK);
        } catch (Exception ex) {
            
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ===== Get Line Chart Data per Region =====
    @GetMapping("/line-chart-data")
    public ResponseEntity<Map<String, List<Integer>>> getLineChartData(HttpServletRequest reques) {
        try {
            Map<String, List<Integer>> lineChartData = dashboardService.computeLineChartData();
            return new ResponseEntity<>(lineChartData, HttpStatus.OK);
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