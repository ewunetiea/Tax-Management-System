package com.afr.fms.Dashboard.maker;





import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.afr.fms.Security.jwt.AllowListProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.afr.fms.Admin.Entity.Region;
import com.afr.fms.Admin.Mapper.BranchMapper;
import com.afr.fms.Admin.Mapper.RegionMapper;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Common.Permission.Mapper.FunctionalitiesMapper;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;

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
    public List<Integer> computeCardData() {
        // description
        // 6 : drafted
        // 0 : waiting for approval
        // 1 : branch manager approved
        // 2 : branch manager rejeced
        return List.of(
            makerDashboardMapper.getCardDataTaxStatus(6),
            makerDashboardMapper.getCardDataTaxStatus(0),
            makerDashboardMapper.getCardDataTaxStatus(1),
            makerDashboardMapper.getCardDataTaxStatus(2)
          
        );
    }




    @Transactional
public List<List<Integer>> computeBarChartDataPerMonth() {
    // SINGLE QUERY - 36x faster!
    List<BarChartRow> allData = makerDashboardMapper.countAllByStatusAndMonthGrouped();
    
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

    //   @Transactional

    //  public List<List<Integer>> computeBarChartDataPerMonth() {
    //     List<List<Integer>> barChartData = new ArrayList<>();

    //     for (int month = 1; month <= 12; month++) {
    //         // int drafted = makerDashboardMapper.countByStatusAndMonth(6, month);

    //         int waiting = makerDashboardMapper.countByStatusAndMonth(0, month);
    //         int reviewed = makerDashboardMapper.countByStatusAndMonth(1, month);
    //         int approved = makerDashboardMapper.countByStatusAndMonth(5, month);

    //         barChartData.add(Arrays.asList( waiting, reviewed, approved));
    //     }


    //     return barChartData;
    // }
    
    @Transactional

 public Map<String, Object> getPolarChartData() {

    System.out.println("_____________EEE________________________________");
    System.out.println(makerDashboardMapper.fetchPolarDataSingleRow());
    return makerDashboardMapper.fetchPolarDataSingleRow();
}


 public List<RadarPayload> getRadarChart() {

    System.out.println("_____________Radar________________________________");
    System.out.println(makerDashboardMapper.getStatusCountsForCurrentAndPreviousYear());
    return makerDashboardMapper.getStatusCountsForCurrentAndPreviousYear();
}



}
