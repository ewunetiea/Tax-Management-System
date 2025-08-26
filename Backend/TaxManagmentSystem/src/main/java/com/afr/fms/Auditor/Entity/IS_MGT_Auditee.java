package com.afr.fms.Auditor.Entity;

import java.time.Instant;
import java.util.List;

import com.afr.fms.Admin.Entity.Branch;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Auditee.Entity.AuditeeDivisionISM;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IS_MGT_Auditee {
    private Long id;
    private Long auditISM_id;
    private Long auditee_id;
    private List<AuditeeDivisionISM> auditeeDivisionISM;
    private List<AuditeeDivisionISM> assignedAuditeeDivisionISM;
    private List<Branch> divisions;
    private Branch auditee;
    private Boolean complete_status;
    private String created_date;
    private String updated_date;
    private Long approver_id;
    private int approval_status;
    private int self_response;
    private int rectification_status;
    private String rectification_date;
    private String finding_status;
    private String approved_date;
    private AuditISM auditISM;
    private List<AuditISM> audits;
    private int auditee_rejected;
    private User rejector;
    private User assigner;
    private String recjection_date;
    private boolean sent_email;
    private Boolean is_email;

}
