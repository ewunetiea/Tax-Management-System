package com.afr.fms.Common.Audit_Remark;

import com.afr.fms.Admin.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Remark {
    private Long id;
    private User sender;
    private User reciever;
    private String message;
    private String remark_date;
    private boolean status;
    private boolean rejected;
    private String starting_url;
}
