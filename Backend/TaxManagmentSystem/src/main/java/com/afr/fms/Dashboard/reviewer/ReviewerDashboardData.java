package com.afr.fms.Dashboard.reviewer;

import java.util.List;

import com.afr.fms.Common.RecentActivity.RecentActivity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewerDashboardData {
    private List<Integer> tax_status;
    private List<RecentActivity> recentActivity;
}
