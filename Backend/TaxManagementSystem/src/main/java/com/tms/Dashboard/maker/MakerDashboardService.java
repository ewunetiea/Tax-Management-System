package com.tms.Dashboard.maker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        // 0 : waiting for review
        // 1 : sent to  approval
        // 2 : branch manager rejeced
         // 3 : approver rejcted
        // 5 : settled



        int status2 = makerDashboardMapper.getCardDataTaxStatus(2, user_id);
        int status3 = makerDashboardMapper.getCardDataTaxStatus(3,user_id);
        System.out.println("_______________" + status2 + " stattt " + " " + status3);
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
    List<BarChartRow> allData = makerDashboardMapper.countAllByStatusAndMonthGrouped(user_id);

    // Group by month and status using streams
    Map<Integer, Map<Integer, Integer>> countsByMonth = allData.stream()
        .collect(Collectors.groupingBy(
            BarChartRow::getMonth,
            Collectors.toMap(
                BarChartRow::getStatus,
                BarChartRow::getCount
            )
        ));

    // Build the bar chart data for all 12 months
    return IntStream.rangeClosed(1, 12)
        .mapToObj(month -> {
            Map<Integer, Integer> monthCounts = countsByMonth.getOrDefault(month, Map.of());
            return List.of(
                monthCounts.getOrDefault(0, 0), // Waiting
                monthCounts.getOrDefault(1, 0), // Reviewed
                monthCounts.getOrDefault(5, 0)  // Approved
            );
        })
        .collect(Collectors.toList());
}



    @Transactional

    public Map<String, Object> getPolarChartData(Long user_id) {

        return makerDashboardMapper.fetchPolarDataSingleRow(user_id);
    }

    public List<RadarPayload> getRadarChart(Long user_id) {

        return makerDashboardMapper.getStatusCountsForCurrentAndPreviousYear(user_id);
    }

 }
