package com.afr.fms.Admin.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminReport {
    private Long numberOfMakers;
    private Long numberOfApprovers;
    private String regionName;
}
