package com.tms.Dashboard.maker;



import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/maker/dashboard")
public class MakerDashboardController {

    @Autowired
    private MakerDashboardService makerDashboardService;

    // ===== Get Card Data =====
    @GetMapping("/card-data/{user_id}")
    public ResponseEntity<List<Integer>> getCardData(@PathVariable("user_id") Long user_id, HttpServletRequest request) {
        try {
            List<Integer> cardData = makerDashboardService.computeCardData(user_id);
            return new ResponseEntity<>(cardData, HttpStatus.OK);
        } catch (Exception ex) {
            
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // ===== Get Bar Chart Data =====
    @GetMapping("/bar-chart/{user_id}")
    public ResponseEntity<List<List<Integer>>> getBarChartData(@PathVariable("user_id") Long user_id, HttpServletRequest request) {
        try {
           List< List<Integer>> barChartData = makerDashboardService.computeBarChartDataPerMonth(user_id);
            return new ResponseEntity<>(barChartData, HttpStatus.OK);
        } catch (Exception ex) {

            
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ===== Get Horizontal Bar Chart Data =====
    @GetMapping("/polar-chart/{user_id}")
    public ResponseEntity<Map<String, Object>> getPolarChartData(@PathVariable("user_id") Long user_id, HttpServletRequest request) {
        try {

Map<String, Object> polarChartData = makerDashboardService.getPolarChartData(user_id);

            return new ResponseEntity<>(polarChartData, HttpStatus.OK);
        } catch (Exception ex) {

            
            
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ===== Get Radar Age Data =====
    @GetMapping("/radar/{user_id}")
    public ResponseEntity<List<RadarPayload>> getRadarAgeData(@PathVariable("user_id") Long user_id, HttpServletRequest reques) {
        try {
          
            return new ResponseEntity<>(makerDashboardService.getRadarChart(user_id), HttpStatus.OK);
        } catch (Exception ex) {
            
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
   
}