package com.afr.fms.Auditor.Dashboard;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Auditor.Entity.IS_MGT_Auditee;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;

@Service
public class AuditorDashboardService {
    @Autowired
    AuditorDashboardMapper auditorDashboardMapper;
    @Autowired
    private RecentActivityMapper recentActivityMapper;

    public AuditorDashboardData getDashBoardData(User user) {
        AuditorDashboardData auditorDashboardData = new AuditorDashboardData();
        List<String> directorates = new ArrayList<>();
        List<Integer> finding_status = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        for (IS_MGT_Auditee is_MGT_Auditee : auditorDashboardMapper.getDirectorates(user.getCategory())) {
            if (!ids.contains(is_MGT_Auditee.getAuditee().getId())
                    && !directorates.contains(is_MGT_Auditee.getAuditee().getName())) {
                ids.add(is_MGT_Auditee.getAuditee().getId());
                directorates.add(is_MGT_Auditee.getAuditee().getName());
                finding_status
                        .add(auditorDashboardMapper.getPassedAuditsPerAuditee(is_MGT_Auditee.getAuditee().getId(),
                                user.getCategory()));
                finding_status
                        .add(auditorDashboardMapper.getReviewedAuditsPerAuditee(is_MGT_Auditee.getAuditee().getId(),
                                user.getCategory()));
                finding_status
                        .add(auditorDashboardMapper.getApprovedAuditsPerAuditee(is_MGT_Auditee.getAuditee().getId(),
                                user.getCategory()));
                finding_status
                        .add(auditorDashboardMapper.getRespondedAuditsPerAuditee(is_MGT_Auditee.getAuditee().getId(),
                                user.getCategory()));

                finding_status
                        .add(auditorDashboardMapper.getPartiallyRectifiedAuditsPerAuditee(
                                is_MGT_Auditee.getAuditee().getId(),
                                user.getCategory()));
                finding_status
                        .add(auditorDashboardMapper.getRectifiedAuditsPerAuditee(is_MGT_Auditee.getAuditee().getId(),
                                user.getCategory()));
            }

        }
        auditorDashboardData.setDirectorates(directorates);
        auditorDashboardData.setFinding_status(finding_status);
        auditorDashboardData.setPolar_data(computePolarData(user.getId()));
        auditorDashboardData.setStacked_bar_chart_data(computeStackedBarChartData(user.getId()));
        auditorDashboardData.setBar_chart_data(computeBarChartData(user));
        auditorDashboardData.setCard_data(computeCardData(user));
        auditorDashboardData.setDoughnut_data(computeDoughnutData(user.getId()));
        auditorDashboardData.setHorizontal_bar_chart_data(computeHorizontalBarChartData(user.getId()));
        auditorDashboardData.setAge_data(computeRadarAgeData(user.getId()));
        auditorDashboardData.setLine_chart_data(computeLineChartData(user.getId()));
        auditorDashboardData.setRecentActivity(recentActivityMapper.getAllRecentActivity(user.getId()));
        return auditorDashboardData;

    }

    public List<Integer> computePolarData(Long auditor_id) {
        List<Integer> polar_data = new ArrayList<>();
        polar_data.add(auditorDashboardMapper.getApprovedCount(auditor_id));
        polar_data.add(auditorDashboardMapper.getApproverRejectedCount(auditor_id));
        polar_data.add(auditorDashboardMapper.getRespondedCount(auditor_id));
        polar_data.add(auditorDashboardMapper.getRectifiedCount(auditor_id));
        polar_data.add(auditorDashboardMapper.getUnrectifiedCount(auditor_id));
        return polar_data;
    }

    public List<Integer> computeDoughnutData(Long auditor_id) {
        List<Integer> doughnut_data = new ArrayList<>();
        doughnut_data.add(auditorDashboardMapper.getTotalFollowupRejectedCounts(auditor_id));
        doughnut_data.add(auditorDashboardMapper.getTotalPartialyRectifiedCounts(auditor_id));
        doughnut_data.add(auditorDashboardMapper.getTotalApprovedCounts(auditor_id));
        doughnut_data.add(auditorDashboardMapper.getTotalRespondedCounts(auditor_id));
        doughnut_data.add(auditorDashboardMapper.getTotalRectifiedCounts(auditor_id));
        return doughnut_data;
    }

    public List<Integer> computeCardData(User user) {
        List<Integer> card_data = new ArrayList<>();
        // card_data.add(auditorDashboardMapper.getTotalDraftingCount(user.getId()));
        // card_data.add(auditorDashboardMapper.getTotalPassedCount(user.getId()));
        // card_data.add(auditorDashboardMapper.getRejectedCount(user.getId()));
        // card_data.add(auditorDashboardMapper.getTotalReviewedCount(user.getId()));

        card_data.add(auditorDashboardMapper.getDraftingCount(user.getId()));
        card_data.add(auditorDashboardMapper.getPassedCount(user.getId()));
        card_data.add(auditorDashboardMapper.getRejectedCount(user.getId()));
        card_data.add(auditorDashboardMapper.getReviewedCount(user.getId()));
        return card_data;
    }

    public List<Integer> computeStackedBarChartData(Long auditor_id) {
        List<Integer> stacked_bar_chart_data = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            stacked_bar_chart_data.add(auditorDashboardMapper.getTotalApprovedAuditsCountPerMonth(i, auditor_id));
            stacked_bar_chart_data
                    .add(auditorDashboardMapper.getTotalApproverRejectedAuditsCountPerMonth(i, auditor_id));
            stacked_bar_chart_data.add(auditorDashboardMapper.getTotalRespondedAuditsCountPerMonth(i, auditor_id));
        }
        return stacked_bar_chart_data;
    }

    public List<Integer> computeLineChartData(Long auditor_id) {
        List<Integer> line_chart_data = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            line_chart_data.add(auditorDashboardMapper.getDraftedAuditsCountPerMonth(i, auditor_id));
            line_chart_data.add(auditorDashboardMapper.getPassedAuditsCountPerMonth(i, auditor_id));
            line_chart_data.add(auditorDashboardMapper.getReviewerRejectedAuditsCountPerMonth(i, auditor_id));
            line_chart_data.add(auditorDashboardMapper.getReviewedAuditsCountPerMonth(i, auditor_id));
        }
        return line_chart_data;
    }

    public List<Integer> computeBarChartData(User user) {
        List<Integer> bar_chart_data = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            bar_chart_data.add(auditorDashboardMapper.getApprovedAuditsCountPerMonth(i, user.getId()));
            bar_chart_data.add(auditorDashboardMapper.getApproverRejectedAuditsCountPerMonth(i, user.getId()));
            bar_chart_data.add(auditorDashboardMapper.getRectifiedAuditsCountPerMonth(i, user.getId()));
            bar_chart_data.add(auditorDashboardMapper.getUnrectifiedAuditsCountPerMonth(i, user.getId()));
        }
        return bar_chart_data;
    }

    public List<Integer> computeRadarAgeData(Long id) {
        List<Integer> age_data = new ArrayList<>();
        int range[] = new int[2];
        for (int i = 0; i < 40; i = range[1]) {
            range[0] = i;
            range[1] = i + 10;
            age_data.add(auditorDashboardMapper.getPassedFindingsAge(id, range));
            age_data.add(auditorDashboardMapper.getApprovedFindingsAge(id, range));
        }
        age_data.add(auditorDashboardMapper.getPassedFindingsAbove40Days(id));
        age_data.add(auditorDashboardMapper.getApprovedFindingsAbove40Days(id));
        return age_data;
    }

    public List<Integer> computeHorizontalBarChartData(Long auditor_id) {
        List<Integer> total_data = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            total_data.add(auditorDashboardMapper.getTotalDraftedAuditsCountPerMonth(i, auditor_id));
            total_data.add(auditorDashboardMapper.getTotalPassedAuditsCountPerMonth(i, auditor_id));
            total_data.add(auditorDashboardMapper.getTotalReviewerRejectedAuditsCountPerMonth(i, auditor_id));
            total_data.add(auditorDashboardMapper.getTotalReviewedAuditsCountPerMonth(i, auditor_id));
        }
        return total_data;
    }

}
