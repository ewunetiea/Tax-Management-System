package com.afr.fms.Admin.Entity;
import lombok.Data;
@Data
public class Backup {
    private String filepath;
    private boolean flag;
    private Long user_id;
}
