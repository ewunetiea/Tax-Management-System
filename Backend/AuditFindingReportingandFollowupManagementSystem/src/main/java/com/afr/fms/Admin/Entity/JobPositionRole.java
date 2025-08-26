package com.afr.fms.Admin.Entity;

import java.util.Collection;

import lombok.Data;

@Data
public class JobPositionRole {
    private Integer id;
    private Role role;
    private Collection<JobPosition> jobPositions;
}
