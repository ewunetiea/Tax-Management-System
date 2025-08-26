
package com.afr.fms.Admin.Dashboard;

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
import com.afr.fms.Common.Permission.Service.FunctionalitiesService;

@RestController
@RequestMapping("/api/admin")
public class AdminDashboardController {
    @Autowired
    private AdminDashboardService adminDashboardService;

    @Autowired
    private FunctionalitiesService functionalitiesService;

    @PostMapping("/dashboard")
    public ResponseEntity<AdminDashboardData> getDashBoardData(@RequestBody User user, HttpServletRequest request) {
        try {
            return new ResponseEntity<>(adminDashboardService.getDashBoardData(user), HttpStatus.OK);
        } catch (Exception ex) {

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
