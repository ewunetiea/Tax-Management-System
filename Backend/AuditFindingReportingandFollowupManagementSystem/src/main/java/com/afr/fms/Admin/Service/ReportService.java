package com.afr.fms.Admin.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Entity.AdminReport;
import com.afr.fms.Admin.Entity.Region;
import com.afr.fms.Admin.Entity.UserTracker;
import com.afr.fms.Admin.Mapper.BranchMapper;
import com.afr.fms.Admin.Mapper.RegionMapper;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Admin.Mapper.UserTrackerMapper;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class ReportService {

    @Autowired
    private RegionMapper regionMapper;
    @Autowired
    private BranchMapper branchMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserTrackerMapper userTrackerMapper;

    public List<Object> drawBranchPerRegionLineChart() {
        List<Object> data = new ArrayList<>();
        for (Region region : regionMapper.getRegions()) {
            data.add(region.getName());
            data.add(branchMapper.getBranchesCountByRegionId(region.getId()));
        }
        return data;
    }

    public List<AdminReport> drawBarChartUsersPerRegion() {
        List<AdminReport> data = new ArrayList<>();
        for (Region region : regionMapper.getRegions()) {
            if (!region.getCode().equalsIgnoreCase("ho")) {
                AdminReport adminReport = new AdminReport();
                adminReport.setRegionName(region.getName());
                adminReport.setNumberOfMakers(userMapper.getMakersPerRegion(region.getId()));
                adminReport.setNumberOfApprovers(userMapper.getApproversPerRegion(region.getId()));
                data.add(adminReport);
            }
        }
        return data;
    }

    public void updateLoginStatus(List<UserTracker> userTrackers) {
        for (UserTracker userTracker : userTrackers) {
            userTrackerMapper.deleteRefreshToken(userTracker.getId());
            userTrackerMapper.updateLoginStatus(userTracker);
        }
    }

    public List<UserTracker> getOnlineFailedUsers(PaginatorPayLoad paginatorPayload) {
        // Ensure that pagination starts before fetching data
        PageHelper.startPage(paginatorPayload.getCurrentPage(), paginatorPayload.getPageSize());

        Page<UserTracker> userPage = (Page<UserTracker>) userTrackerMapper.getOnlineFailedUsers();

        List<UserTracker> userTrackers = userPage.getResult();

        if (!userTrackers.isEmpty()) {
            userTrackers.get(0).setTotal_records_paginator(userPage.getTotal());
        }

        return userTrackers;
    }

}
