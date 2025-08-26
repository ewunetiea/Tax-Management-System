package com.afr.fms.Admin.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Branch {
    private Long id;
    private String name;
    private String code;
    private Region region;
    private boolean status;
    private Long user_id;
    private int page_number; // for pagination purpose only, it will not be saved in db, just for UI  pagination
    private int page_size; // for pagination purpose only, it will not be saved in db, just for UI pagination
    private Long total_records; // for pagination purpose only, it will not be saved in db, just for UI  pagination
    private Long total_records_paginator;
}
