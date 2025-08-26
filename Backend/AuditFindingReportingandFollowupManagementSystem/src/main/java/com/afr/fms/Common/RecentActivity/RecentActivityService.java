package com.afr.fms.Common.RecentActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.afr.fms.Admin.Entity.Role;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Common.Entity.Report;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecentActivityService {

    List<String> store = new ArrayList<>();

    @Autowired
    private RecentActivityMapper recentActivityMapper;

    @Autowired
    private UserMapper userMapper;

    public void addRecentActivity(RecentActivity ra) {
        recentActivityMapper.addRecentActivity(ra);
    }

    public List<RecentActivity> getRecentActivityByUserId(Long user_id) {
        Role role = ((List<Role>) userMapper.getUserById(user_id).getRoles()).get(0);
        if (role.getName().equalsIgnoreCase("ROLE_ADMIN")) {
            return recentActivityMapper.getAllAdminRecentActivity(user_id);
        }
        return recentActivityMapper.getAllRecentActivity(user_id);
    }

    public List<RecentActivity> getRecentActivityAdmin(Report report) {
        String user_ids = "";
        if (report.getUser_ids() != null) {

            user_ids = StringUtils.join(report.getUser_ids(), ',');

            if (report.getUser_ids().size() == 0) {
                report.setUser_ids(null);
            }
        }

        if (isNotNull(report.getUser_ids()) && populateStore("user_ids")
                && areFieldsNull(report, store)) {
            return recentActivityMapper.getActivityByUserIDs(user_ids);
        } else if (isNotNull(report.getUser_ids()) && isNotNull(report.getContent())
                && populateStore("user_ids,content")
                && areFieldsNull(report, store)) {
            recentActivityMapper.getActivityByUserIDsandContent(report, user_ids);
        } else if (isNotNull(report.getUser_ids()) && isNotNull(report.getAction_date())
                && populateStore("user_ids,action_date")
                && areFieldsNull(report, store)) {
            recentActivityMapper.getActivityByUserIDsandActionDate(report, user_ids);
        } else if (isNotNull(report.getUser_ids()) && isNotNull(report.getAction_date())
                && isNotNull(report.getContent())
                && populateStore("user_ids,action_date,content")
                && areFieldsNull(report, store)) {
            recentActivityMapper.getActivityByUserIDsandContentandActionDate(report, user_ids);
        } else if (isNotNull(report.getAction_date()) && populateStore("action_date")
                && areFieldsNull(report, store)) {
            return recentActivityMapper.getActivityByDateRangeAdmin(report);
        } else if (isNotNull(report.getAction_date()) && isNotNull(report.getContent())
                && populateStore("action_date,content")
                && areFieldsNull(report, store)) {
            return recentActivityMapper.getActivityByDateRangeandContentAdmin(report);
        } else if (isNotNull(report.getContent())
                && populateStore("content")
                && areFieldsNull(report, store)) {
            return recentActivityMapper.getActivityByContentAdmin(report);
        }
        return null;
        // return recentActivityMapper.getAllRecentActivityAdmin(report);
    }

    public List<RecentActivity> getAllRecentActivityByUserId(Report report) {
        return recentActivityMapper.getAllRecentActivityByUserId(report);
    }

    public List<RecentActivity> getActivityByDateAndContent(Report report) {

        // if (report.getAction_date() != null && report.getContent() == null) {
        // return recentActivityMapper.getActivityByDateRange(report);
        // } else if (report.getAction_date() == null && report.getContent() != null) {
        // return recentActivityMapper.getActivityByContent(report);

        // } else if (report.getAction_date() != null && report.getContent() != null) {
        // return recentActivityMapper.getActivityByDateRangeandContent(report);
        // }
        // return null;

        if (isNotNull(report.getAction_date()) && populateStore("action_date,user_id")
                && areFieldsNull(report, store)) {
            return recentActivityMapper.getActivityByDateRange(report);
        } else if (isNotNull(report.getAction_date()) && isNotNull(report.getContent())
                && populateStore("action_date,content,user_id")
                && areFieldsNull(report, store)) {
            return recentActivityMapper.getActivityByDateRangeandContent(report);
        } else if (isNotNull(report.getContent())
                && populateStore("content,user_id")
                && areFieldsNull(report, store)) {
            return recentActivityMapper.getActivityByContent(report);
        }
        return null;
    }

    public boolean populateStore(String var) {
        store = new ArrayList<>();
        List<String> list = Arrays.stream(var.split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        for (String list1 : list) {
            store.add(list1);
        }

        return true;
    }

    public boolean isNotNull(Object object) {
        return object != null;
    }

    public boolean areFieldsNull(Report object, List<String> fieldsToSkip) {

        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (!fieldsToSkip.contains(field.getName()) && field.get(object) != null) {
                    return false;

                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

}
