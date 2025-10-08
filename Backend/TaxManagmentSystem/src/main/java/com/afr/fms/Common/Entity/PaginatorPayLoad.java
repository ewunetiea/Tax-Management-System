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
    private int pageSize;
    private String event_length;
    private Long branch_id;
}
