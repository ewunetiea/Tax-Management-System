package com.tms.Dashboard.reviewer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewerDashboardService {
    @Autowired
    private ReviewerDashboardMapper reviewerDashboardMapper;

//     @Transactional
//     public Map<String, Object> getTaxStatusForReviewer(Long branch_id) {
//         Map<String, Object> counts = reviewerDashboardMapper.getTaxStatusForReviewer(branch_id);
// System.out.println("________counts_________________" + branch_id);
//         System.out.println(counts);
//         if (counts == null) {
//             counts = Map.of(   
//                     "Pending", 0,
//                     "Reviewed", 0,
//                     "Approved", 0,
//                     "Rejected", 0);
//         }
//         return counts;
//     }




@Transactional
public Map<String, Object> getTaxStatusForReviewer(Long branch_id) {
    Map<String, Object> counts =
            reviewerDashboardMapper.getTaxStatusForReviewer(branch_id);

            System.out.println("________counts_________________" + branch_id);
        System.out.println(counts);

    if (counts == null || counts.isEmpty()) {
        counts = new LinkedHashMap<>();
        counts.put("Pending", 0);
        counts.put("Reviewed", 0);
        counts.put("Approved", 0);
        counts.put("Rejected", 0);
    }

    return counts;
}


    // Tax Status for stacked bar (monthly)
    public List<Map<String, Object>> getStackedBarTaxesStatusData(Long branch_id) {
        return reviewerDashboardMapper.getStackedBarTaxesStatusData(branch_id);
    }

}
