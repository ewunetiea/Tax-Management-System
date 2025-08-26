package com.afr.fms.Auditor.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Audit {
    private Long id;
    private String category;
    private Boolean complete_status;
    private Boolean review_status;
    private Boolean approve_status;
}
