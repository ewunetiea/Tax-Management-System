package com.afr.fms.Approver.Dashboard;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Auditor.Entity.IS_MGT_Auditee;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;

@Service
public class ApproverDashboardService {
    @Autowired
    ApproverDashboardMapper approverDashboardMapper;
    @Autowired
    private RecentActivityMapper recentActivityMapper;

    public ApproverDashboardData getDashBoardData(User user) {
        ApproverDashboardData approverDashboardData = new ApproverDashboardData();
        List<String> directorates = new ArrayList<>();
        List<Integer> finding_status = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        for (IS_MGT_Auditee is_MGT_Auditee : approverDashboardMapper.getDirectorates(user.getCategory())) {
            if (!ids.contains(is_MGT_Auditee.getAuditee().getId())
                    && !directorates.contains(is_MGT_Auditee.getAuditee().getName())) {
                ids.add(is_MGT_Auditee.getAuditee().getId());
                directorates.add(is_MGT_Auditee.getAuditee().getName());
                finding_status
                        .add(approverDashboardMapper.getApprovedAuditsPerAuditee(is_MGT_Auditee.getAuditee().getId(),
                                user.getCategory()));
                finding_status
                        .add(approverDashboardMapper.getRespondedAuditsPerAuditee(is_MGT_Auditee.getAuditee().getId(),
                                user.getCategory()));
                finding_status
                        .add(approverDashboardMapper.getPartiallyRectifiedAuditsPerAuditee(
                                is_MGT_Auditee.getAuditee().getId(),
                                user.getCategory()));
                finding_status
                        .add(approverDashboardMapper.getRectifiedAuditsPerAuditee(is_MGT_Auditee.getAuditee().getId(),
                                user.getCategory()));
            }

        }
        approverDashboardData.setDirectorates(directorates);
        approverDashboardData.setFinding_status(finding_status);
        approverDashboardData.setPolar_data(computePolarData(user));
        approverDashboardData.setStacked_bar_chart_data(computeStackedBarChartData(user));
        approverDashboardData.setBar_chart_data(computeBarChartData(user));
        approverDashboardData.setCard_data(computeCardData(user));
        approverDashboardData.setDoughnut_data(computeDoughnutData(user));
        approverDashboardData.setHorizontal_bar_chart_data(computeHorizontalBarChartData(user));
        approverDashboardData.setAge_data(computeRadarAgeData(user));
        approverDashboardData.setRecentActivity(recentActivityMapper.getAllRecentActivity(user.getId()));

        return approverDashboardData;
    }

    public List<Integer> computePolarData(User user) {
        List<Integer> polar_data = new ArrayList<>();
        polar_data.add(approverDashboardMapper.getRejectedCount(user));
        polar_data.add(approverDashboardMapper.getApprovedCount(user));
        // polar_data.add(approverDashboardMapper.getUnrectifiedCount(user));
        polar_data.add(approverDashboardMapper.getTotalUnapprovedCount(user));
        return polar_data;
    }

    public List<Integer> computeDoughnutData(User user) {
        List<Integer> doughnut_data = new ArrayList<>();
        // doughnut_data.add(approverDashboardMapper.getAuditeeHandCounts(user));
        // doughnut_data.add(approverDashboardMapper.getTotalRespondedCounts(user));
        doughnut_data.add(approverDashboardMapper.getRectifiedCount(user));
        doughnut_data.add(approverDashboardMapper.getPartiallyRectifiedCounts(user));
        doughnut_data.add(approverDashboardMapper.getTotalUnrectifiedCounts(user));
        return doughnut_data;
    }

    public List<Integer> computeCardData(User user) {
        List<Integer> card_data = new ArrayList<>();
        card_data.add(approverDashboardMapper.getTotalPendingCount(user.getCategory()));
        card_data.add(approverDashboardMapper.getTotalApprovedFindingsCount(user));
        card_data.add(approverDashboardMapper.getRespondedCountsCard(user));
        card_data.add(approverDashboardMapper.getRectifiedCount(user));
        return card_data;
    }

    public List<Integer> computeStackedBarChartData(User user) {
        List<Integer> stacked_bar_chart_data = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            stacked_bar_chart_data.add(approverDashboardMapper.getRespondedAuditsCountPerMonth(i, user));
            stacked_bar_chart_data.add(approverDashboardMapper.getPartiallyRectifiedAuditsCountPerMonth(i, user));
            stacked_bar_chart_data.add(approverDashboardMapper.getRectifiedAuditsCountPerMonth(i, user));
            stacked_bar_chart_data.add(approverDashboardMapper.getUnrectifiedAuditsCountPerMonth(i, user));
            // stacked_bar_chart_data.add(approverDashboardMapper.getFollowupRejectedAuditsCountPerMonth(i,
            // user));
        }
        return stacked_bar_chart_data;
    }

    public List<Integer> computeBarChartData(User user) {
        List<Integer> bar_chart_data = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            bar_chart_data.add(approverDashboardMapper.getPendingAuditsCountPerMonth(i, user.getCategory()));
            bar_chart_data.add(approverDashboardMapper.getApprovedAuditsCountPerMonth(i, user));
            bar_chart_data.add(approverDashboardMapper.getRejectedCountPerMonth(i, user));
        }
        return bar_chart_data;
    }

    public List<Integer> computeRadarAgeData(User user) {
        List<Integer> age_data = new ArrayList<>();
        int range[] = new int[2];
        for (int i = 0; i < 40; i = range[1]) {
            range[0] = i;
            range[1] = i + 10;
            age_data.add(approverDashboardMapper.getApprovedFindingsAge(user, range));
            age_data.add(approverDashboardMapper.getRespondedFindingsAge(user, range));
        }
        age_data.add(approverDashboardMapper.getApprovedFindingsAbove40Dayes(user));
        age_data.add(approverDashboardMapper.getRespondedFindingsAbove40Dayes(user));
        return age_data;
    }

    public List<Integer> computeHorizontalBarChartData(User user) {
        List<Integer> total_data = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            total_data.add(approverDashboardMapper.getTotalPendingAuditsCountPerMonth(i, user.getCategory()));
            total_data.add(approverDashboardMapper.getTotalApprovedAuditsCountPerMonth(i, user));
            total_data.add(approverDashboardMapper.getRejectedAuditsCountPerMonth(i, user));
            total_data.add(approverDashboardMapper.getTotalRespondedAuditsCountPerMonth(i, user));
        }
        return total_data;
    }

}
