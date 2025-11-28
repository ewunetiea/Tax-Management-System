package com.tms.Dashboard.maker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.transaction.Transactional;

import com.tms.Security.jwt.AllowListProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tms.Admin.Entity.Region;
import com.tms.Admin.Mapper.BranchMapper;
import com.tms.Admin.Mapper.RegionMapper;
import com.tms.Admin.Mapper.UserMapper;
import com.tms.Common.Permission.Mapper.FunctionalitiesMapper;
import com.tms.Common.RecentActivity.RecentActivity;
import com.tms.Common.RecentActivity.RecentActivityMapper;

@Service
public class MakerDashboardService {

    @Autowired
    private MakerDashboardMapper makerDashboardMapper;

    @Autowired
    private RecentActivityMapper recentActivityMapper;

    @Autowired
    private RegionMapper regionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BranchMapper branchMapper;

    @Autowired
    private FunctionalitiesMapper functionalitiesMapper;

    @Transactional

    // Card data
    public List<Integer> computeCardData(Long user_id) {
        // description
        // 6 : drafted
        // 0 : waiting for approval
        // 1 : branch manager approved
        // 2 : branch manager rejeced

        int status2 = makerDashboardMapper.getCardDataTaxStatus(2, user_id);
        int status3 = makerDashboardMapper.getCardDataTaxStatus(3,user_id);
        return List.of(
                makerDashboardMapper.getCardDataTaxStatus(6, user_id),
                makerDashboardMapper.getCardDataTaxStatus(0, user_id),
                makerDashboardMapper.getCardDataTaxStatus(1, user_id),
                // makerDashboardMapper.getCardDataTaxStatus(2)
                status2 + status3

        );
    }

    @Transactional
    public List<List<Integer>> computeBarChartDataPerMonth(Long user_id) {
        // SINGLE QUERY - 36x faster!
        List<BarChartRow> allData = makerDashboardMapper.countAllByStatusAndMonthGrouped(user_id);

        // Build result map
        Map<Integer, Map<Integer, Integer>> countsByMonth = new HashMap<>();
        for (BarChartRow row : allData) {
            countsByMonth.computeIfAbsent(row.getMonth(), k -> new HashMap<>())
                    .put(row.getStatus(), row.getCount());
        }

        List<List<Integer>> barChartData = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            Map<Integer, Integer> monthCounts = countsByMonth.getOrDefault(month, new HashMap<>());
            int waiting = monthCounts.getOrDefault(0, 0);
            int reviewed = monthCounts.getOrDefault(1, 0);
            int approved = monthCounts.getOrDefault(5, 0);
            barChartData.add(Arrays.asList(waiting, reviewed, approved));
        }
        return barChartData;
    }

    @Transactional

    public Map<String, Object> getPolarChartData(Long user_id) {

        return makerDashboardMapper.fetchPolarDataSingleRow(user_id);
    }

    public List<RadarPayload> getRadarChart(Long user_id) {

        return makerDashboardMapper.getStatusCountsForCurrentAndPreviousYear(user_id);
    }

 }
