package com.tms.Common.Entity;

import java.util.List;

import com.tms.Admin.Entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Report {
    private Long user_id;
    private User user;
    private String startDateTime;
    private String endDateTime;
    private Long region_id;
    private Long branch_id;
    private String content;
    private List<String> action_date;
    private Long age;
    private List<Long> age_range;
    private List<Long>user_ids;
    private List<User>users;

}