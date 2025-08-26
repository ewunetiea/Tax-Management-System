package com.afr.fms.Common.RecentActivity;

import java.sql.Date;
import java.util.List;

import com.afr.fms.Admin.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecentActivity {
    private Long id;
    private String message;
    private Date created_date;
    private List<String> action_date;
    private User user;
}
