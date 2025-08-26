package com.afr.fms.Common.Dashboard.Higher_Officials.Models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalsForStatChartE {
    Double reported;
    Double approved;
    Double rectified;
    Double unrectified;
}
