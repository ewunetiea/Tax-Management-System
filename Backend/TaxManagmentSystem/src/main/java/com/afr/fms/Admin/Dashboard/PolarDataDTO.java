package com.afr.fms.Admin.Dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor

@NoArgsConstructor
public class PolarDataDTO {
    private int active;
    private int inactive;
    private int accountLocked;
    private int credentialExpired;

    // getters and setters
}