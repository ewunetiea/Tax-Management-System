package com.afr.fms.Common.Dashboard.Higher_Officials.Models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnrectifiedForAllModulesE {
    String title;
    Double very_low;
    Double low;
    Double medium;
    Double high;
    Double very_high;
}
