package com.tms.Dashboard.maker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor

@NoArgsConstructor
public class RadarPayload {
    private int year;
    private int drafted;
    private int waitingForReview;
    private int reviewed;
    private int reviewerRejected;
    private int approved;
    private int approverRejected;

    // Getters and Setters
}

