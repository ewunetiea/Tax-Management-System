package com.afr.fms.Common.RecentActivity;

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
    private String created_date;
    private User user;
}
