
package com.afr.fms.Auditee.DivisionDashboard;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Approver.Dashboard.ApproverDashboardData;

@RestController
@RequestMapping("/api/division")
public class DivisionDashboardController {
    @Autowired
    private DivisionDashboardService divisionDashboardService;

    @PostMapping("/dashboard/ism")
    public ResponseEntity<ApproverDashboardData> getDashBoardData(@RequestBody User user, HttpServletRequest request) {
        try {
            return new ResponseEntity<>(divisionDashboardService.getDashBoardData(user), HttpStatus.OK);
        } catch (Exception ex) {
            System.out.println(ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
