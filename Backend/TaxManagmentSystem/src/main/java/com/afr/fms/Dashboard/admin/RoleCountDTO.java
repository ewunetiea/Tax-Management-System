package com.afr.fms.Dashboard.admin;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor

@NoArgsConstructor
public class RoleCountDTO {
    private String rolePosition;
    private String code;
    private int count;

    // getters and setters
}