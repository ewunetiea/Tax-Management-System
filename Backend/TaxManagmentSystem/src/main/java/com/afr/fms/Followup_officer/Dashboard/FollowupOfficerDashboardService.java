package com.afr.fms.Followup_officer.Dashboard;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Auditor.Entity.IS_MGT_Auditee;
import com.afr.fms.Common.RecentActivity.RecentActivityService;

@Service
public class FollowupOfficerDashboardService {
    @Autowired
    FollowupOfficerDashboardMapper followupOfficerDashboardMapper;

    @Autowired
    RecentActivityService recentActivityService;

    public FollowupOfficerDashboardData getDashBoardData(User user) {
        FollowupOfficerDashboardData followupOfficerDashboardData = new FollowupOfficerDashboardData();
        List<String> directorates = new ArrayList<>();
        List<Integer> finding_status = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        for (IS_MGT_Auditee is_MGT_Auditee : followupOfficerDashboardMapper.getDirectorates(user.getCategory())) {
            if (!ids.contains(is_MGT_Auditee.getAuditee().getId())
                    && !directorates.contains(is_MGT_Auditee.getAuditee().getName())) {
                ids.add(is_MGT_Auditee.getAuditee().getId());
                directorates.add(is_MGT_Auditee.getAuditee().getName());

                finding_status
                        .add(followupOfficerDashboardMapper
                                .getRespondedAuditsPerAuditee(is_MGT_Auditee.getAuditee().getId(), user.getCategory()));
                finding_status
                        .add(followupOfficerDashboardMapper
                                .getRectifiedAuditsPerAuditee(is_MGT_Auditee.getAuditee().getId(), user.getCategory()));
                finding_status
                        .add(followupOfficerDashboardMapper
                                .getPartiallyRectifiedAuditsPerAuditee(is_MGT_Auditee.getAuditee().getId(),
                                        user.getCategory()));
                finding_status
                        .add(followupOfficerDashboardMapper
                                .getRejectedAuditsPerAuditee(is_MGT_Auditee.getAuditee().getId(), user.getCategory()));
                finding_status
                        .add(followupOfficerDashboardMapper.getUnrectifiedAuditsPerAuditee(
                                is_MGT_Auditee.getAuditee().getId(), user.getCategory()));
            }

        }
        followupOfficerDashboardData.setDirectorates(directorates);
        followupOfficerDashboardData.setFinding_status(finding_status);
        followupOfficerDashboardData.setPolar_data(computePolarData(user));
        followupOfficerDashboardData.setStacked_bar_chart_data(computeStackedBarChartData(user));
        followupOfficerDashboardData.setCard_data(computeCardData(user));
        followupOfficerDashboardData.setDoughnut_data(computeDoughnutData(user.getCategory()));
        followupOfficerDashboardData.setHorizontal_bar_chart_data(computeHorizontalBarChartData(user.getCategory()));
        followupOfficerDashboardData.setAge_data(computeRadarAgeData(user));
        followupOfficerDashboardData.setRecentActivity(recentActivityService.getRecentActivityByUserId(user.getId()));
        return followupOfficerDashboardData;
    }

    public List<Integer> computePolarData(User user) {
        List<Integer> polar_data = new ArrayList<>();
        polar_data.add(followupOfficerDashboardMapper.getReviewedCount(user.getCategory()));
        polar_data.add(followupOfficerDashboardMapper.getApprovedCount(user.getCategory()));
        polar_data.add(followupOfficerDashboardMapper.getRespondedCount(user.getCategory()));
        polar_data.add(followupOfficerDashboardMapper.getRectifiedCountCurrent(user.getId()));
        polar_data.add(followupOfficerDashboardMapper.getPartiallyRectifiedCountCurrent(user.getId()));
        return polar_data;
    }

    public List<Integer> computeDoughnutData(String category) {
        List<Integer> doughnut_data = new ArrayList<>();
        doughnut_data.add(followupOfficerDashboardMapper.getTotalReviewedCounts(category));
        doughnut_data.add(followupOfficerDashboardMapper.getTotalApprovedCounts(category));
        doughnut_data.add(followupOfficerDashboardMapper.getTotalRespondedCounts(category));
        return doughnut_data;
    }

    public List<Integer> computeCardData(User user) {
        List<Integer> card_data = new ArrayList<>();
        card_data.add(followupOfficerDashboardMapper.getPendingCount(user.getCategory()));
        card_data.add(followupOfficerDashboardMapper.getRejectedCount(user.getId()));
        card_data.add(followupOfficerDashboardMapper.getRectifiedCount(user.getId()));
        card_data.add(followupOfficerDashboardMapper.getUnrectifiedCount(user.getId()));
        return card_data;
    }

    public List<Integer> computeStackedBarChartData(User user) {
        List<Integer> stacked_bar_chart_data = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            stacked_bar_chart_data
                    .add(followupOfficerDashboardMapper.getPendingAuditsCountPerMonth(i, user.getCategory()));
            stacked_bar_chart_data.add(followupOfficerDashboardMapper.getRectifiedAuditsCountPerMonth(i, user.getId()));
            stacked_bar_chart_data
                    .add(followupOfficerDashboardMapper.getPartiallyRectifiedAuditsCountPerMonth(i, user.getId()));
            stacked_bar_chart_data.add(followupOfficerDashboardMapper.getRejectedAuditsCountPerMonth(i, user.getId()));
            stacked_bar_chart_data
                    .add(followupOfficerDashboardMapper.getUnrectifiedAuditsCountPerMonth(i, user.getId()));
        }
        return stacked_bar_chart_data;
    }

    public List<Integer> computeRadarAgeData(User user) {
        List<Integer> age_data = new ArrayList<>();
        int range[] = new int[2];
        for (int i = 0; i < 40; i = range[1]) {
            range[0] = i;
            range[1] = i + 10;
            age_data.add(followupOfficerDashboardMapper.getRejectedFindingsAge(user.getId(), range));
            age_data.add(followupOfficerDashboardMapper.getRespondedFindingsAge(user.getCategory(),range));
        }
        age_data.add(followupOfficerDashboardMapper.getRejectedFindingsAbove40Days(user.getId()));
        age_data.add(followupOfficerDashboardMapper.getRespondedFindingsAbove40Days(user.getCategory()));
        return age_data;
    }

    public List<Integer> computeHorizontalBarChartData(String category) {
        List<Integer> total_data = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            total_data.add(followupOfficerDashboardMapper.getTotalReviewedAuditsCountPerMonth(i, category));
            total_data.add(followupOfficerDashboardMapper.getTotalApprovedAuditsCountPerMonth(i, category));
            total_data.add(followupOfficerDashboardMapper.getTotalRespondedAuditsCountPerMonth(i, category));
        }
        return total_data;
    }

}
