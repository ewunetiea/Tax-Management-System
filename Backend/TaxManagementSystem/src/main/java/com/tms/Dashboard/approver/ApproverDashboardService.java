package com.tms.Dashboard.approver;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApproverDashboardService {
    @Autowired
    private ApproverDashboardMapper approverDashboardMapper;

    // Tax Status for card
    @Transactional
    public Map<String, Object> getTaxStatusForApprover(Long branch_id) {
        Map<String, Object> counts = approverDashboardMapper.getTaxStatusForApprover(branch_id);
        if (counts == null) {
            counts = Map.of(
                    "Pending", 0,
                    "Approved", 0,
                    "Rejected", 0);
        }
        return counts;
    }

    // Tax Status for stacked bar (monthly)
    public List<Map<String, Object>> getStackedBarTaxesStatusData(Long branch_id) {
        return approverDashboardMapper.getStackedBarTaxesStatusDataForApprover(branch_id);
    }
    
}
