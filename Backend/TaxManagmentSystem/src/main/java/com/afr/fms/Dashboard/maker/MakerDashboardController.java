package com.afr.fms.Dashboard.maker;



import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.afr.fms.Common.RecentActivity.RecentActivity;

@RestController
@RequestMapping("/api/maker/dashboard")
public class MakerDashboardController {

    @Autowired
    private MakerDashboardService makerDashboardService;

    // ===== Get Card Data =====
    @GetMapping("/card-data")
    public ResponseEntity<List<Integer>> getCardData(HttpServletRequest request) {
        try {
            List<Integer> cardData = makerDashboardService.computeCardData();
            return new ResponseEntity<>(cardData, HttpStatus.OK);
        } catch (Exception ex) {
            
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // ===== Get Bar Chart Data =====
    @GetMapping("/bar-chart")
    public ResponseEntity<List<List<Integer>>> getBarChartData(HttpServletRequest request) {
        try {
           List< List<Integer>> barChartData = makerDashboardService.computeBarChartDataPerMonth();
            return new ResponseEntity<>(barChartData, HttpStatus.OK);
        } catch (Exception ex) {

            System.out.println(ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ===== Get Horizontal Bar Chart Data =====
    @GetMapping("/polar-chart")
    public ResponseEntity<Map<String, Object>> getPolarChartData(HttpServletRequest request) {
        try {

Map<String, Object> polarChartData = makerDashboardService.getPolarChartData();

            return new ResponseEntity<>(polarChartData, HttpStatus.OK);
        } catch (Exception ex) {

            System.out.println(ex);
            
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ===== Get Radar Age Data =====
    @GetMapping("/radar")
    public ResponseEntity<List<RadarPayload>> getRadarAgeData(HttpServletRequest reques) {
        try {
          
            return new ResponseEntity<>(makerDashboardService.getRadarChart(), HttpStatus.OK);
        } catch (Exception ex) {
            
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
   
}