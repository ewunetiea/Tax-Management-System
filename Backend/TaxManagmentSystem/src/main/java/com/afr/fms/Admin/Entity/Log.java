package com.afr.fms.Admin.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log {
    
    private Long id;
    private String name;
    private String exception;
    private String log_time;
 

}
