package com.afr.fms.Dashboard.maker;





import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    private static final List<String> AUDITS = List.of("IS", "MGT", "INS", "BFA");


    // Line chart
    public Map<String, List<Integer>> computeLineChartData() {
        List<Region> regions = regionMapper.getRegions();
        Map<String, List<Integer>> lineChartData = new LinkedHashMap<>(regions.size());

        for (Region region : regions) {
            List<Integer> userCounts = new ArrayList<>(AUDITS.size());
            for (String audit : AUDITS) {
                userCounts.add(makerDashboardMapper.getUsersPerRegion(audit, region.getId()));
            }
            lineChartData.put(region.getName(), userCounts);
        }
        return lineChartData;
    }

    // ✅ Polar data (now uses single query)
    public List<Integer> computePolarData() {
        Map<String, Integer> counts = makerDashboardMapper.getPolarDataCounts();
        return List.of(
            counts.getOrDefault("active_users", 0),
            counts.getOrDefault("inactive_users", 0),
            counts.getOrDefault("account_locked_users", 0),
            counts.getOrDefault("acredential_expired_users", 0)
        );
    }

    // Card data
    public List<Integer> computeCardData() {
        // description
        // 6 : drafted
        // 0 : waiting for approval
        // 1 : branch manager approved
        // 2 : branch manager rejeced
        return List.of(
            makerDashboardMapper.getTaxStatus(6),
            makerDashboardMapper.getTaxStatus(0),
            makerDashboardMapper.getTaxStatus(1),
            makerDashboardMapper.getTaxStatus(2)
          
        );
    }

    // Bar chart
    public List<Integer> computeBarChartData() {
        List<String> role_positions = List.of("IS", "MGT", "INS");
        List<String> roles = List.of("Approver", "Auditor", "Followup", "Reviewer");
        List<Integer> bar_chart_data = new ArrayList<>();
        for (String rolePosition : role_positions) {
            for (String code : roles) {
                bar_chart_data.add(makerDashboardMapper.getUsersPerRoleandAudit(rolePosition, code));
            }
        }
        return bar_chart_data;
    }

    // Horizontal bar chart
    public List<Integer> computeHorizontalBarChartData() {
        List<String> roles = List.of("Auditor", "Reviewer", "COMPILER", "Approver", "BRANCHM", "REGIONALD");
        List<Integer> bar_chart_data = new ArrayList<>();

        for (String code : roles) {
            bar_chart_data.add(makerDashboardMapper.getUsersPerRoleandAudit("BFA", code));
        }
        return bar_chart_data;
    }

    // Radar age data
    public List<Integer> computeRadarAgeData() {
        return List.of(
            makerDashboardMapper.getUserActiveStatusPerAudit(1, "IS"),
            makerDashboardMapper.getUserActiveStatusPerAudit(0, "IS"),
            makerDashboardMapper.getUserActiveStatusPerAudit(1, "MGT"),
            makerDashboardMapper.getUserActiveStatusPerAudit(0, "MGT"),
            makerDashboardMapper.getUserActiveStatusPerAudit(1, "BFA"),
            makerDashboardMapper.getUserActiveStatusPerAudit(0, "BFA"),
            makerDashboardMapper.getUserActiveStatusPerAudit(1, "INS"),
            makerDashboardMapper.getUserActiveStatusPerAudit(0, "INS")
        );
    }

    // Recent activity
    public List<RecentActivity> getRecentActivity(Long userId) {
        return recentActivityMapper.getAllRecentActivity(userId);
    }
}
