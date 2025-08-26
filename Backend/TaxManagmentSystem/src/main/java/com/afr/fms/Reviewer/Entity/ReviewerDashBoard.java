package com.afr.fms.Reviewer.Entity;

import java.util.List;

import com.afr.fms.Common.RecentActivity.RecentActivity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewerDashBoard {

    public List<String> directorates;
    public List<Integer> finding_status;
    public List<Integer> polar_data;
    public List<Integer> barchart_data;
    public List<Integer> linechart_age_data;
    public List<Integer> card_data;
    public List<Integer> doughnut_data;
    public List<Integer> stacked_barchart_data;
    public List<Integer> horizontal_barchart_data;
    private List<RecentActivity> recentActivity;

}