package com.afr.fms.Dashboard.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor

@NoArgsConstructor
public class RegionUserCountDTO {
    private String category;
    private String regionName;
    private int count;

    // getters and setters
}