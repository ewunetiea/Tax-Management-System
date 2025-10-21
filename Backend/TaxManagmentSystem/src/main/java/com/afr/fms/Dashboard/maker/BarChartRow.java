package com.afr.fms.Dashboard.maker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor

@NoArgsConstructor
public class BarChartRow {
   private int month;
    private int status;
    private int count;

    // getters and setters
}
