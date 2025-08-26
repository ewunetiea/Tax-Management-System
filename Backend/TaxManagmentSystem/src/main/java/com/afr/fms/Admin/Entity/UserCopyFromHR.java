package com.afr.fms.Admin.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCopyFromHR {
    private Integer id;
    private Boolean status;
    private String deptLocation;
    private String departmentName;
    private String empId;
    private String position;
    private String empName;
    private String email;
    private String employmentDate;
    private String unit;
}
