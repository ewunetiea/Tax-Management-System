package com.afr.fms.Admin.Controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.afr.fms.Admin.Entity.Schedule;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Admin.Service.ScheduleService;

import com.afr.fms.Security.jwt.JwtUtils;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    JwtUtils jwtUtils;

    private User user;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/schedule")
    public ResponseEntity<List<Schedule>> getSchedules(HttpServletRequest request) {
        if (scheduleService.verifyPermission(request, "view_all_schedules")) {
            try {
                return new ResponseEntity<>(scheduleService.getSchedules(), HttpStatus.OK);
            } catch (Exception ex) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/schedule")
    public ResponseEntity<Boolean> updateScheduleStatus(@RequestBody String schedule_status,
            HttpServletRequest request) {
        if (scheduleService.verifyPermission(request, "update_schedule_status")) {
            try {
                String jwt = jwtUtils.getJwtFromCookies(request);
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                user = userMapper.findByEmail(username);
                return new ResponseEntity<Boolean>(
                        scheduleService.updateScheduleStatus(schedule_status, user), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
