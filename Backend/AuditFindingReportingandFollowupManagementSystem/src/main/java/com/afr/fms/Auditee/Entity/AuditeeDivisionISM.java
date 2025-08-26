package com.afr.fms.Auditee.Entity;

import java.util.List;

import com.afr.fms.Admin.Entity.Branch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditeeDivisionISM {
    private Long id;
    private String action_plan;
    private String previous_action_plan;
    private Long division_id;
    private Long responder_id;
    private AuditeeDivisionFileISM attached_files;
    private AuditeeDivisionFileISM previously_attached_files;
    private List<AuditeeDivisionFileISM> uploaded_files;
    private List<AuditeeDivisionFileISM> previously_uploaded_files;
    private Long IS_MGT_Auditee_id;
    private Branch division;
    private Boolean file_edited;
    private String created_date;
    private String response_subimtted_date;
    private Boolean response_status;
    private Long audit_id;
    private int response_count;
    private Boolean file_flag;
    private Boolean submitted_auditee;
    private Boolean is_edited;
    private int self_response;
    private int rectification_status;
    private int auditee_submitted;
    private String scheduled_date;
    private String case_number;
    private Boolean is_auditee;
}
