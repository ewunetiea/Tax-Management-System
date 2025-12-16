package com.tms.Admin.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tms.Admin.Entity.AdminReport;
import com.tms.Admin.Entity.Region;
import com.tms.Admin.Entity.UserTracker;
import com.tms.Admin.Mapper.BranchMapper;
import com.tms.Admin.Mapper.RegionMapper;
import com.tms.Admin.Mapper.UserMapper;
import com.tms.Admin.Mapper.UserTrackerMapper;
import com.tms.Common.Entity.PaginatorPayLoad;


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

        List<UserTracker> userTrackers =  userTrackerMapper.getOnlineFailedUsers();
;

       
        return userTrackers;
    }

}
