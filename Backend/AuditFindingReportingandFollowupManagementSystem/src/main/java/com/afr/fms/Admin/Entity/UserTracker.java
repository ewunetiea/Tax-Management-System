package com.afr.fms.Admin.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserTracker {
    private Long id;
    private String user_name;
    private String user_agent;
    private String ip_address;
    private String login_time;
    private int status;
    private User user;
    private Long total_records_paginator;
}
