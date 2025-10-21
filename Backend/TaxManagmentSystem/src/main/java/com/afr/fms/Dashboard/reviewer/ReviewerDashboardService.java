package com.afr.fms.Dashboard.reviewer;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewerDashboardService {
    @Autowired
    private ReviewerDashboardMapper reviewerDashboardMapper;

    // Tax Status for card
    public List<Integer> getTaxStatusForReviewer(Long branch_id) {
        Map<String, Integer> counts = reviewerDashboardMapper.getTaxStatusCounts(branch_id);
        return List.of(
                counts.getOrDefault("Pending", 0),
                counts.getOrDefault("Reviewed", 0),
                counts.getOrDefault("Approved", 0),
                counts.getOrDefault("Rejected", 0));
    }

    // Tax Status for stacked bar (monthly)
    public List<Map<String, Object>> getStackedBarTaxesStatusData(Long branch_id) {
        return reviewerDashboardMapper.getStackedBarTaxesStatusData(branch_id);
    }

}
