package com.afr.fms.Admin.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class NotifyAdmin {
    private Long id;
    private String user_name;
    private boolean failed;
    private boolean locked;
    private boolean expired;
    private int failed_status;
    private int locked_status;
    private int expired_status;
}
