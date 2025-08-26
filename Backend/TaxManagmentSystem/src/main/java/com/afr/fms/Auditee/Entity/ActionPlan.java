package com.afr.fms.Auditee.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionPlan {
    
    private Long id;
    private Long auditee_id;
    private String content;
    private String created_date;
    private String updated_date;

    
}
