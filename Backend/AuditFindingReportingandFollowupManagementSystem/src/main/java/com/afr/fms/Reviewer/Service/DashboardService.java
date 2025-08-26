package com.afr.fms.Reviewer.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Auditor.Entity.IS_MGT_Auditee;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;
import com.afr.fms.Reviewer.Entity.ReviewerDashBoard;
import com.afr.fms.Reviewer.Mapper.ReviewerDashboardMapper;

@Service
public class DashboardService {
    @Autowired
    ReviewerDashboardMapper reviewerDashBoardMapper;
    @Autowired
    RecentActivityMapper recentActivityMapper;

    public ReviewerDashBoard getDashboardData(User user) {
        ReviewerDashBoard reviewerDashBoard = new ReviewerDashBoard();
        List<String> directorates = new ArrayList<>();
        List<Integer> finding_status = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        for (IS_MGT_Auditee is_MGT_Auditee : reviewerDashBoardMapper.getDirectorates(user.getCategory())) {
            if (!ids.contains(is_MGT_Auditee.getAuditee().getId())
                    && !directorates.contains(is_MGT_Auditee.getAuditee().getName())) {
                ids.add(is_MGT_Auditee.getAuditee().getId());
                directorates.add(is_MGT_Auditee.getAuditee().getName());
                finding_status
                        .add(reviewerDashBoardMapper.getReviewedAudits(is_MGT_Auditee.getAuditee().getId(),
                                user.getCategory()));
                finding_status
                        .add(reviewerDashBoardMapper.getApprovedAudits(is_MGT_Auditee.getAuditee().getId(),
                                user.getCategory()));
                finding_status
                        .add(reviewerDashBoardMapper.getRespondedAudits(is_MGT_Auditee.getAuditee().getId(),
                                user.getCategory()));
                finding_status
                        .add(reviewerDashBoardMapper.getPartiallyRectifiedAudits(is_MGT_Auditee.getAuditee().getId(),
                                user.getCategory()));
                finding_status
                        .add(reviewerDashBoardMapper.getRectifiedAudits(is_MGT_Auditee.getAuditee().getId(),
                                user.getCategory()));
            }
        }
        reviewerDashBoard.setDirectorates(directorates);
        reviewerDashBoard.setFinding_status(finding_status);
        reviewerDashBoard.setPolar_data(getPolarChartDataCurrentFindingStatus(user.getId()));
        reviewerDashBoard.setBarchart_data(drawBarChartCurrentFindingStatus(user.getId()));
        reviewerDashBoard.setLinechart_age_data(computeRadarAgeData(user.getId()));
        reviewerDashBoard.setCard_data(populateCardData(user));
        reviewerDashBoard.setStacked_barchart_data(drawStackedBarChartCurrentFindingStatusOtherRoles(user.getId()));
        reviewerDashBoard.setHorizontal_barchart_data(drawHorizontalBarChartTotalFindingStatus(user.getId()));
        reviewerDashBoard.setDoughnut_data(getDoughnutTotalFindingStatusData(user.getId()));
        reviewerDashBoard.setRecentActivity(recentActivityMapper.getAllRecentActivity(user.getId()));

        return reviewerDashBoard;
    }

    public List<Integer> getPolarChartDataCurrentFindingStatus(Long reviewer_id) {

        List<Integer> polarData = new ArrayList<>();
        polarData.add(reviewerDashBoardMapper.getRejectedFindings(reviewer_id));
        polarData.add(reviewerDashBoardMapper.getApproverRejectedFindings(reviewer_id));
        polarData.add(reviewerDashBoardMapper.getUnrectifiedFindings(reviewer_id));
        polarData.add(reviewerDashBoardMapper.getFollowupRejectedFindings(reviewer_id));
        return polarData;
    }

    public List<Integer> drawBarChartCurrentFindingStatus(Long id) {
        List<Integer> data = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            data.add(reviewerDashBoardMapper.getPendingFindingsPerMonth(i, id));
            data.add(reviewerDashBoardMapper.getReviewedFindingsPerMonth(i, id));
            data.add(reviewerDashBoardMapper.getRejectedFindingsPerMonth(i, id));
        }
        return data;
    }

    public List<Integer> drawStackedBarChartCurrentFindingStatusOtherRoles(Long id) {
        List<Integer> data = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            data.add(reviewerDashBoardMapper.getApprovedFindingsPerMonth(i, id));
            data.add(reviewerDashBoardMapper.getRespondedFindingsPerMonth(i, id));
            data.add(reviewerDashBoardMapper.getPartiallyRectifiedFindingsPerMonth(i, id));
            data.add(reviewerDashBoardMapper.getRectifiedFindingsPerMonth(i, id));
            data.add(reviewerDashBoardMapper.getUnrectifiedFindingsPerMonth(i, id));

        }

        return data;
    }

    public List<Integer> drawHorizontalBarChartTotalFindingStatus(Long id) {
        List<Integer> data = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            data.add(reviewerDashBoardMapper.getTotalApprovedFindingsPerMonth(i, id));
            data.add(reviewerDashBoardMapper.getTotalRespondedFindingsPerMonth(i, id));
            data.add(reviewerDashBoardMapper.getPartiallyRectifiedFindingsPerMonth(i, id));
            data.add(reviewerDashBoardMapper.getRectifiedFindingsPerMonth(i, id));
            data.add(reviewerDashBoardMapper.getUnrectifiedFindingsPerMonth(i, id));
        }

        return data;
    }

    public List<Integer> computeRadarAgeData(Long id) {
        List<Integer> age_data = new ArrayList<>();
        int range[] = new int[2];
        for (int i = 0; i < 40; i = range[1]) {
            range[0] = i;
            range[1] = i + 10;
            age_data.add(reviewerDashBoardMapper.getReviewedFindingsAge(id, range));
            age_data.add(reviewerDashBoardMapper.getApprovedFindingsAge(id, range));
        }
        age_data.add(reviewerDashBoardMapper.getReviewedFindingsAbove40Days(id));
        age_data.add(reviewerDashBoardMapper.getApprovedFindingsAbove40Days(id));
        return age_data;
    }

    // public List<Integer> drawLineChartFindingWaitingResponseAge(Long id) {
    // List<Integer> data = new ArrayList<>();
    // data.add(reviewerDashBoardMapper.getFindings_1_10_Dayes(id));
    // data.add(reviewerDashBoardMapper.getFindings_11_20_Dayes(id));
    // data.add(reviewerDashBoardMapper.getFindings21_30_Dayes(id));
    // data.add(reviewerDashBoardMapper.getFindings31_40_Dayes(id));
    // data.add(reviewerDashBoardMapper.getFindingsAbove_40_Dayes(id));
    // return data;
    // }

    public List<Integer> populateCardData(User user) {
        List<Integer> data = new ArrayList<>();
        if (user.getCategory().trim().equalsIgnoreCase("IS")) {
            data.add(reviewerDashBoardMapper.getPendingFindings(user.getCategory()));
        } else {
            data.add(reviewerDashBoardMapper.getPendingFindingsMGT(user.getBanking()));
        }
        data.add(reviewerDashBoardMapper.getReviewedFindings(user.getId()));
        data.add(reviewerDashBoardMapper.getApprovedFindings(user.getId()));
        data.add(reviewerDashBoardMapper.getRectifiedFindings(user.getId()));
        return data;
    }

    public List<Integer> getDoughnutTotalFindingStatusData(Long reviewer_id) {

        List<Integer> polarData = new ArrayList<>();
        polarData.add(reviewerDashBoardMapper.getApprovedFindingsCount(reviewer_id));
        polarData.add(reviewerDashBoardMapper.getTotalRespondedFindings(reviewer_id));
        polarData.add(reviewerDashBoardMapper.getPartiallyRectifiedFindings(reviewer_id));
        polarData.add(reviewerDashBoardMapper.getFullyRectifiedFindings(reviewer_id));
        return polarData;
    }

}
