package com.tms.Dashboard.approver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

  

// public List<Map<String, Object>> getStackedBarTaxesStatusData(Long branch_id) {
//     List<Map<String, Object>> dbRows =
//         approverDashboardMapper.getStackedBarTaxesStatusDataForApprover(branch_id);

//     Map<Integer, Map<Integer, Integer>> countsByMonth = new HashMap<>();

//     for (Map<String, Object> row : dbRows) {
//         Number monthNum = (Number) row.get("month");
//         Number statusNum = (Number) row.get("status");
//         Number countNum = (Number) row.get("count");

//         if (monthNum == null || statusNum == null || countNum == null) continue;

//         int month  = monthNum.intValue();
//         int status = statusNum.intValue();
//         int count  = countNum.intValue();

//         countsByMonth.computeIfAbsent(month, k -> new HashMap<>())
//                      .put(status, count);
//     }

//     List<Map<String, Object>> result = new ArrayList<>();
//     for (int month = 1; month <= 12; month++) {
//         Map<Integer, Integer> monthCounts = countsByMonth.getOrDefault(month, Map.of());

//         Map<String, Object> row = new HashMap<>();
//         row.put("month", month);
//         row.put("Pending",  monthCounts.getOrDefault(1, 0));
//         row.put("Approved", monthCounts.getOrDefault(5, 0));
//         row.put("Rejected", monthCounts.getOrDefault(3, 0));

//         result.add(row);
//     }

//     return result;
// }



public List<Map<String, Object>> getStackedBarTaxesStatusData(Long branch_id) {
    List<Map<String, Object>> dbRows = approverDashboardMapper.getStackedBarTaxesStatusDataForApprover(branch_id);

    // Group counts by month and status using streams
    Map<Integer, Map<Integer, Integer>> countsByMonth = dbRows.stream()
        .filter(row -> row.get("month") != null && row.get("status") != null && row.get("count") != null)
        .collect(Collectors.groupingBy(
            row -> ((Number) row.get("month")).intValue(),
            Collectors.toMap(
                row -> ((Number) row.get("status")).intValue(),
                row -> ((Number) row.get("count")).intValue()
            )
        ));

    // Build final result for all 12 months
    return IntStream.rangeClosed(1, 12)
        .mapToObj(month -> {
            Map<Integer, Integer> monthCounts = countsByMonth.getOrDefault(month, Map.of());
            Map<String, Object> row = new HashMap<>();
            row.put("month", month);
            row.put("Pending",  monthCounts.getOrDefault(1, 0));
            row.put("Approved", monthCounts.getOrDefault(5, 0));
            row.put("Rejected", monthCounts.getOrDefault(3, 0));
            return row;
        })
        .collect(Collectors.toList());
}


    
}
