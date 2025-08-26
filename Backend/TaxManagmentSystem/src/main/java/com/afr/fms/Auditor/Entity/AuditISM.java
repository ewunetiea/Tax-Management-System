package com.afr.fms.Auditor.Entity;

import java.util.List;

import com.afr.fms.Admin.Entity.Branch;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Auditee.Entity.AuditeeDivisionISM;
import com.afr.fms.Common.Audit_Change_Tracker.ISM.Change_Tracker_ISM;
import com.afr.fms.Model.MGT.AssetAndLiability;
import com.afr.fms.Model.MGT.CreditDocumentationParent;
import com.afr.fms.Model.MGT.LoanAndAdvance;
import com.afr.fms.Model.MGT.MemorandomAndContingent;
import com.afr.fms.Model.MGT.SuspenseAccount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditISM {
   private Long id;
   private String case_number;
   private User auditor;
   private List<User> auditors;
   private User reviewer;
   private User followup_officer;
   private User approver;
   private User auditee;
   private String finding_date;
   private String finding;
   private String finding_detail;
   private double amount;
   private String cash_type;
   private String fcy;
   private String impact;
   private String recommendation;
   private String risk_level;
   private int review_status;
   private int approve_status;
   private String rectification_date;
   private String unrectification_date;
   private int rectification_status;
   private String category;
   private int status;
   private Boolean save_template;
   private Boolean edit_auditee;
   private List<Branch> auditees;
   private String created_date;
   private String updated_date;
   private List<IS_MGT_Auditee> IS_MGTAuditee;
   private Boolean response_added;
   private Boolean division_assigned;
   private String reviewer_edit_date;
   private String approver_edit_date;
   private String followup_rejected_date;
   private String reviewer_rejected_date;
   private String approver_rejected_date;
   private String audit_date;
   private Boolean is_edited;
   private List<Change_Tracker_ISM> change_tracker_ISM;
   private User Editor;
   private String finding_status;
   private String approved_date;
   private String reviewed_date;
   private String responded_date;
   private Boolean auditor_status;
   private Boolean auditee_submitted;
   private List<String> file_urls;
   List<User> users;
   private Long auditee_id;
   private Long directorate_id;
   private String auditee_name;
   private int auditee_rectification_status;
   private String auditee_rectification_date;
   private String auditee_unrectification_date;
   private int auditee_responded_added;
   private String auditee_responded_date;
   private int auditee_division_assigned;
   private int division_response_added;
   private boolean divisionresponseadded;
   private String division_response_date;
   private String division_name;
   private Long division_auditee_id;
   private String management;
   private Boolean is_email;
   private List<AuditeeDivisionISM> auditeeDivisionISM;
   private int page_number; // for pagination purpose only, it will not be saved in db, just for UI
                            // pagination
   private int page_size; // for pagination purpose only, it will not be saved in db, just for UI
                          // pagination
   private Long total_records; // for pagination purpose only, it will not be saved in db, just for UI
                               // pagination
   private List<String> finding_dates;
   private List<String> approved_dates;
   private List<String> rectification_dates;
   private String role_name;
   private String rectification_approval_status;
   private String rectification_approval_date;
   private String rejection_status;
   private String auditee_finding_status;
   private String[] division_names;
   private Long total_records_paginator;
   private AssetAndLiability assetAndLiability;
   private MemorandomAndContingent memorandomAndContingent;
   private SuspenseAccount suspenseAccount;
   private AbnormalBalance abnormalBalance;
   private LoanAndAdvance creditDocumentation;
   private String audit_type;
   private LongOutstandingItems longOutstandingItems;
   private CashCount cashCount;
   private CreditDocumentationParent creditDocumentationParent;
   private String target_division_name;

}
