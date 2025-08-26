package com.afr.fms.Common.Audit_Change_Tracker.ISM;

import com.afr.fms.Admin.Entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Change_Tracker_ISM {
    private Long id;
    private User changer;
    private String change;
    private Long audit_id;
    private String change_date;
    private String content_type;
}
