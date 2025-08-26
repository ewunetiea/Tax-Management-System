package com.afr.fms.Auditee.DivisionDashboard;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Approver.Dashboard.ApproverDashboardData;
import com.afr.fms.Common.RecentActivity.RecentActivityService;

@Service
public class DivisionDashboardService {
    @Autowired
    DivisionDashboardMapper divisionDashboardMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    RecentActivityService recentActivityService;

    public ApproverDashboardData getDashBoardData(User user) {
        user = userMapper.getAuditorById(user.getId());
        ApproverDashboardData divisionDashboardData = new ApproverDashboardData();
        divisionDashboardData.setPolar_data(computePolarData(user.getBranch().getId()));
        divisionDashboardData.setStacked_bar_chart_data(computeStackedBarChartData(user.getBranch().getId()));
        divisionDashboardData.setBar_chart_data(computeLineChartData(user.getBranch().getId()));
        divisionDashboardData.setCard_data(computeCardData(user.getBranch().getId()));
        divisionDashboardData.setHorizontal_bar_chart_data(computeHorizontalBarChartData(user.getBranch().getId()));
        divisionDashboardData.setAge_data(computeRadarAgeData(user.getBranch().getId()));
        divisionDashboardData.setRecentActivity(recentActivityService.getRecentActivityByUserId(user.getId()));
        return divisionDashboardData;
    }

    public List<Integer> computePolarData(Long auditee_id) {
        List<Integer> polar_data = new ArrayList<>();
        polar_data.add(divisionDashboardMapper.getRectifiedCount(auditee_id));
        polar_data.add(divisionDashboardMapper.getPartiallyRectifiedCount(auditee_id));
        polar_data.add(divisionDashboardMapper.getTotalPartiallyRectifiedCount(auditee_id));
        polar_data.add(divisionDashboardMapper.getUnrectifiedCount(auditee_id));
        polar_data.add(divisionDashboardMapper.getRejectedCount(auditee_id));
        return polar_data;
    }

    public List<Integer> computeCardData(Long auditee_id) {
        List<Integer> card_data = new ArrayList<>();
        card_data.add(divisionDashboardMapper.getPendingCount(auditee_id));
        card_data.add(divisionDashboardMapper.getOnProgressCount(auditee_id));
        card_data.add(divisionDashboardMapper.getRespondedCount(auditee_id));
        card_data.add(divisionDashboardMapper.getFinishedCount(auditee_id));
        return card_data;
    }

    public List<Integer> computeStackedBarChartData(Long auditee_id) {
        List<Integer> stacked_bar_chart_data = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            stacked_bar_chart_data.add(divisionDashboardMapper.getPendingAuditsCountPerMonth(i, auditee_id));
            stacked_bar_chart_data.add(divisionDashboardMapper.getRespondedAuditsCountPerMonth(i, auditee_id));
            stacked_bar_chart_data.add(divisionDashboardMapper.getFinishedAuditsCountPerMonth(i, auditee_id));
        }
        return stacked_bar_chart_data;
    }

    public List<Integer> computeLineChartData(Long auditee_id) {
        List<Integer> bar_chart_data = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            bar_chart_data.add(divisionDashboardMapper.getRectifiedAuditsCountPerMonth(i, auditee_id));
            bar_chart_data.add(divisionDashboardMapper.getPartiallyRectifiedAuditsCountPerMonth(i, auditee_id));
            bar_chart_data.add(divisionDashboardMapper.getUnrectifiedAuditsCountPerMonth(i, auditee_id));
            // bar_chart_data.add(divisionDashboardMapper.getRejectedAuditsCountPerMonth(i,
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
            age_data.add(divisionDashboardMapper.getApprovedFindingsAge(id, range));
            age_data.add(divisionDashboardMapper.getRespondedFindingsAge(id, range));
        }
        age_data.add(divisionDashboardMapper.getApprovedFindingsAbove40Dayes(id));
        age_data.add(divisionDashboardMapper.getRespondedFindingsAbove40Dayes(id));
        return age_data;
    }

    public List<Integer> computeHorizontalBarChartData(Long auditee_id) {
        List<Integer> total_data = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            total_data.add(divisionDashboardMapper.getTotalPendingAuditsCountPerMonth(i, auditee_id));
            total_data.add(divisionDashboardMapper.getTotalRespondedAuditsCountPerMonth(i, auditee_id));
            total_data.add(divisionDashboardMapper.getTotalFinishedAuditsCountPerMonth(i, auditee_id));
        }
        return total_data;
    }

}
