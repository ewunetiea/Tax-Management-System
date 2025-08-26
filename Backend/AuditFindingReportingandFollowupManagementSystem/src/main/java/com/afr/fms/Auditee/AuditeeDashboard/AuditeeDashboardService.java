package com.afr.fms.Auditee.AuditeeDashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Approver.Dashboard.ApproverDashboardData;
import com.afr.fms.Auditee.Entity.AuditeeDivisionISM;
import com.afr.fms.Common.RecentActivity.RecentActivityService;

@Service
public class AuditeeDashboardService {
    @Autowired
    AuditeeDashboardMapper auditeeDashboardMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    RecentActivityService recentActivityService;

    public ApproverDashboardData getDashBoardData(User user) {
        user = userMapper.getAuditorById(user.getId());
        ApproverDashboardData auditeeDashboardData = new ApproverDashboardData();
        auditeeDashboardData.setPolar_data(computePolarData(user.getBranch().getId()));

        Map<String, List<?>> result = computeStackedBarChartData(user.getBranch().getId());
        List<String> divisions = (List<String>) result.get("divisions");
        List<Integer> stackedBarChartData = (List<Integer>) result.get("stackedBarChartData");
        auditeeDashboardData.setStacked_bar_chart_data(stackedBarChartData);
        auditeeDashboardData.setDivisions(divisions);

        auditeeDashboardData.setBar_chart_data(computeLineChartData(user.getBranch().getId()));
        auditeeDashboardData.setCard_data(computeCardData(user.getBranch().getId()));
        auditeeDashboardData.setHorizontal_bar_chart_data(computeHorizontalBarChartData(user.getBranch().getId()));
        auditeeDashboardData.setAge_data(computeRadarAgeData(user.getBranch().getId()));
        auditeeDashboardData.setRecentActivity(recentActivityService.getRecentActivityByUserId(user.getId()));
        return auditeeDashboardData;
    }

    public List<Integer> computePolarData(Long auditee_id) {
        List<Integer> polar_data = new ArrayList<>();
        polar_data.add(auditeeDashboardMapper.getRectifiedCount(auditee_id));
        polar_data.add(auditeeDashboardMapper.getPartiallyRectifiedCount(auditee_id));
        polar_data.add(auditeeDashboardMapper.getTotalPartiallyRectifiedCount(auditee_id));
        polar_data.add(auditeeDashboardMapper.getUnrectifiedCount(auditee_id));
        polar_data.add(auditeeDashboardMapper.getRejectedCount(auditee_id));
        return polar_data;
    }

    public List<Integer> computeCardData(Long auditee_id) {
        List<Integer> card_data = new ArrayList<>();
        card_data.add(auditeeDashboardMapper.getPendingCount(auditee_id));
        card_data.add(auditeeDashboardMapper.getOnProgressCount(auditee_id));
        card_data.add(auditeeDashboardMapper.getRespondedCount(auditee_id));
        card_data.add(auditeeDashboardMapper.getFinishedCount(auditee_id));
        return card_data;
    }

    public Map<String, List<?>> computeStackedBarChartData(Long auditee_id) {

        List<String> divisions = new ArrayList<>();
        List<Integer> stackedBarChartData = new ArrayList<>();

        for (AuditeeDivisionISM auditeeDivisionISM : auditeeDashboardMapper.getDivisions(auditee_id)) {

            System.out.println(auditeeDivisionISM);
            if (auditeeDivisionISM == null)
                continue;
            if (!divisions.contains(auditeeDivisionISM.getDivision().getName())) {

                divisions.add(auditeeDivisionISM.getDivision().getName());
                stackedBarChartData.add(auditeeDashboardMapper
                        .getOnProgressAuditsCountPerDivision(auditeeDivisionISM.getDivision_id(), auditee_id));
                stackedBarChartData.add(auditeeDashboardMapper
                        .getRespondedAuditsCountPerDivision(auditeeDivisionISM.getDivision_id(), auditee_id));
                stackedBarChartData.add(
                        auditeeDashboardMapper.getFinishedAuditsCountPerDivision(auditeeDivisionISM.getDivision_id(),
                                auditee_id));
                stackedBarChartData.add(auditeeDashboardMapper
                        .getPartiallyRectifiedAuditsCountPerDivision(auditeeDivisionISM.getDivision_id(), auditee_id));
                stackedBarChartData.add(
                        auditeeDashboardMapper.getUnrectifiedAuditsCountPerDivision(auditeeDivisionISM.getDivision_id(),
                                auditee_id));
            }
        }

        Map<String, List<?>> result = new HashMap<>();
        result.put("divisions", divisions);
        result.put("stackedBarChartData", stackedBarChartData);

        return result;
    }

    public List<Integer> computeLineChartData(Long auditee_id) {
        List<Integer> bar_chart_data = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            bar_chart_data.add(auditeeDashboardMapper.getRectifiedAuditsCountPerMonth(i, auditee_id));
            bar_chart_data.add(auditeeDashboardMapper.getPartiallyRectifiedAuditsCountPerMonth(i, auditee_id));
            bar_chart_data.add(auditeeDashboardMapper.getUnrectifiedAuditsCountPerMonth(i, auditee_id));
            // bar_chart_data.add(auditeeDashboardMapper.getRejectedAuditsCountPerMonth(i,
            // auditee_id));
        }
        return bar_chart_data;
    }

    public List<Integer> computeRadarAgeData(Long id) {
        List<Integer> age_data = new ArrayList<>();
        int range[] = new int[2];
        for (int i = 0; i < 40; i = range[1]) {
            range[0] = i;
            range[1] = i + 10;
            age_data.add(auditeeDashboardMapper.getApprovedFindingsAge(id, range));
            age_data.add(auditeeDashboardMapper.getRespondedFindingsAge(id, range));
        }
        age_data.add(auditeeDashboardMapper.getApprovedFindingsAbove40Dayes(id));
        age_data.add(auditeeDashboardMapper.getRespondedFindingsAbove40Dayes(id));
        return age_data;
    }

    public List<Integer> computeHorizontalBarChartData(Long auditee_id) {
        List<Integer> total_data = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            total_data.add(auditeeDashboardMapper.getTotalPendingAuditsCountPerMonth(i, auditee_id));
            total_data.add(auditeeDashboardMapper.getTotalRespondedAuditsCountPerMonth(i, auditee_id));
            total_data.add(auditeeDashboardMapper.getTotalFinishedAuditsCountPerMonth(i, auditee_id));
        }
        return total_data;
    }

}
