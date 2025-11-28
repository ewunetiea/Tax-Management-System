package com.tms.Admin.Controller;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tms.Admin.Entity.Schedule;
import com.tms.Admin.Entity.User;
import com.tms.Admin.Mapper.UserMapper;
import com.tms.Admin.Service.ScheduleService;
import com.tms.Security.jwt.JwtUtils;

@RestController
@RequestMapping("/api")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    JwtUtils jwtUtils;

    private User user;

    @Autowired
    private UserMapper userMapper;

    private static final Logger logger = LoggerFactory.getLogger(ScheduleController.class);

    @GetMapping("/schedule")
    public ResponseEntity<List<Schedule>> getSchedules(HttpServletRequest request) {
        try {
            return new ResponseEntity<>(scheduleService.getSchedules(), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error fetching schedules: ", ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/schedule")
    public ResponseEntity<HttpStatus> updateScheduleStatus(@RequestBody List<Schedule> schedule_status, HttpServletRequest request) {
        try {
            String jwt = jwtUtils.getJwtFromCookies(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            user = userMapper.findByEmail(username);
            scheduleService.updateScheduleStatus(schedule_status, user);
            return new ResponseEntity<HttpStatus>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error updating schedule status: ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
