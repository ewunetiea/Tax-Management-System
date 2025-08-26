
package com.afr.fms.Approver.Dashboard;

import java.util.List;

import com.afr.fms.Common.RecentActivity.RecentActivity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApproverDashboardData {
    private List<Integer> card_data;
    private List<Integer> polar_data;
    public List<String> directorates;
    public List<String> divisions;
    public List<Integer> finding_status;
    public List<Integer> stacked_bar_chart_data;
    public List<Integer> bar_chart_data;
    public List<Integer> horizontal_bar_chart_data;
    public List<Integer> doughnut_data;
    public List<Integer> age_data;
    private List<RecentActivity> recentActivity;

}
