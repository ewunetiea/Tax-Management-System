package com.afr.fms.Admin.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Region {
    private Long id;
    private String name;
    private String code;
    private boolean status;
    private Long user_id;
}
