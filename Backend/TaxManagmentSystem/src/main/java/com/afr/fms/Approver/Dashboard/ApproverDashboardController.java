
package com.afr.fms.Approver.Dashboard;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Payload.endpoint.Endpoint;


@RestController
@RequestMapping("/api/approver")
public class ApproverDashboardController {
    @Autowired
    private ApproverDashboardService approverDashboardService;

    @PostMapping("/dashboard/ism")
    public ResponseEntity<ApproverDashboardData> getDashBoardData(@RequestBody User user, HttpServletRequest request) {
        try {
            return new ResponseEntity<>(approverDashboardService.getDashBoardData(user), HttpStatus.OK);
        } catch (Exception ex) {
            System.out.println(ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
