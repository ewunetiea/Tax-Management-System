package com.tms.Common.RecentActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tms.Admin.Entity.User;
import com.tms.Common.Entity.Report;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecentActivityService {

    List<String> store = new ArrayList<>();

    @Autowired
    private RecentActivityMapper recentActivityMapper;

    public void addRecentActivity(RecentActivity ra) {
        recentActivityMapper.addRecentActivity(ra);
    }

    public List<RecentActivity> generateRecentActivities(Report report) {
        List<String> actionDates = report.getAction_date();
        List<Date> parsedActionDates = null;
        if (actionDates != null && actionDates.size() == 2) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                Date startDate = dateFormat.parse(actionDates.get(0));
                Date endDate = dateFormat.parse(actionDates.get(1));
                parsedActionDates = Arrays.asList(startDate, endDate); // Create a list using Arrays.asList()
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return recentActivityMapper.generateRecentActivities(report.getUser_ids(), report.getContent(),
                parsedActionDates);
    }

    public List<RecentActivity> getAllRecentActivities(RecentActivity recentActivity) {
        List<String> actionDates = recentActivity.getAction_date();
        List<Date> parsedActionDates = null;
        if (actionDates != null && actionDates.size() == 2) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                Date startDate = dateFormat.parse(actionDates.get(0));
                Date endDate = dateFormat.parse(actionDates.get(1));
                parsedActionDates = Arrays.asList(startDate, endDate); // Create a list using Arrays.asList()
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return recentActivityMapper.getAllRecentActivities(recentActivity.getUser().getId(),
                recentActivity.getMessage(),
                parsedActionDates);

    }

    public List<RecentActivity> getRecentActivityByUserId(Long user_id) {
        return recentActivityMapper.getAllRecentActivity(user_id);
    }

    public List<User> getUsers() {
        return recentActivityMapper.getUsers();
    }

     public List<RecentActivity> getAllRecentActivityByUserId(Long user_id) {
        return recentActivityMapper.getAllRecentActivityByUserId(user_id);
    }

}
