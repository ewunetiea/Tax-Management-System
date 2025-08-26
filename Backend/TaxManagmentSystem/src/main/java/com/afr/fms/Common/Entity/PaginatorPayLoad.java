package com.afr.fms.Common.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatorPayLoad {
    private Long user_id;
    private int totalRecords;;
    private int currentPage; 
    private String event_length;
    private int pageSize;
    private String category;
    private String  audit_status;
    private String banking;
    private String audit_type;
    private String user_role;
}
