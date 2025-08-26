package com.afr.fms.Admin.Entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JobPosition {
    private Long id;

    private String title;

    private Integer location;

    private List<Role> roles;
}
