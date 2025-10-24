package com.afr.fms.Dashboard.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;

@Service
public class AdminDashboardService {

    @Autowired
    private AdminDashboardMapper adminDashboardMapper;

    @Autowired
    private RecentActivityMapper recentActivityMapper;

    // Card data
    public List<Integer> computeCardData() {
        return adminDashboardMapper.computeCardData();
    }

    public Map<String, Integer> getDougnutData() {
        return adminDashboardMapper.getDougnutDataCounts();
    }

    // Recent activity
    public List<RecentActivity> getRecentActivity(Long userId) {
        return recentActivityMapper.getAllRecentActivity(userId);
    }

    public Map<String, List<?>> getRegionBranchDashboardData() {
        List<Map<String, Object>> data = adminDashboardMapper.getBranchCountPerRegion();

        List<String> regions = new ArrayList<>();
        List<Integer> branchCounts = new ArrayList<>();

        for (Map<String, Object> row : data) {
            regions.add((String) row.get("region_name")); // cast to String
            branchCounts.add(((Number) row.get("branch_count")).intValue()); // cast to Number then int
        }

        Map<String, List<?>> branchPerRegion = new HashMap<>();
        branchPerRegion.put("regions", regions);
        branchPerRegion.put("branchCounts", branchCounts);

        return branchPerRegion;
    }
}
