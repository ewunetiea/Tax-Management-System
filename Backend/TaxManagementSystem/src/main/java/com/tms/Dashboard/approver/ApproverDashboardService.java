package com.tms.Dashboard.approver;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApproverDashboardService {
    @Autowired
    private ApproverDashboardMapper approverDashboardMapper;

    // Tax Status for card
    public List<Integer> getTaxStatusForApprover(Long branch_id) {
        Map<String, Integer> counts = approverDashboardMapper.getTaxStatusForApprover(branch_id);
        return List.of(
                counts.getOrDefault("Pending", 0),
                counts.getOrDefault("Approved", 0),
                counts.getOrDefault("Rejected", 0));
    }

    // Tax Status for stacked bar (monthly)
    public List<Map<String, Object>> getStackedBarTaxesStatusData(Long branch_id) {
        return approverDashboardMapper.getStackedBarTaxesStatusDataForApprover(branch_id);
    }
}
