package com.afr.fms.Auditor.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.afr.fms.Admin.Entity.Branch;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Entity.IS_MGT_Auditee;
import com.afr.fms.Auditor.Mapper.AuditISMMapper;
import com.afr.fms.Auditor.Mapper.CommonActionAuditorMapper;
import com.afr.fms.Auditor.Mapper.GeneeralObservationAuditorMapper;
import com.afr.fms.Auditor.Mapper.UploadFileISMMapper;
import com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMService;
import com.afr.fms.Common.Audit_Change_Tracker.ISM.Change_Tracker_ISM;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import java.util.List;

@Service
public class GeneeralObservationAuditorService {
    @Autowired
    private GeneeralObservationAuditorMapper geneeralObservationAuditorMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RecentActivityMapper recentActivityMapper;

    @Autowired
    private UploadFileISMMapper uploadFileISMMapper;

    @Autowired
    private AuditISMMapper auditeeMapper;

    @Autowired
    private ChangeTrackerISMService changeTrackerISMService;
    @Autowired

    private CommonActionAuditorMapper commonActionAuditorMapper;

    private RecentActivity recentActivity;

    public void createGeneralObservationFinding(AuditISM audit) {
        User user = userMapper.getAuditorById(audit.getAuditor().getId());
        audit.setCategory(user.getCategory());
        audit.setCase_number(generateCaseNumber(audit.getCategory()));
        Long audit_id = (long) 0;
            audit_id = geneeralObservationAuditorMapper.createGeneralObservationFinding(audit);
       

        IS_MGT_Auditee IS_MGTAuditee = new IS_MGT_Auditee();
        IS_MGTAuditee.setAuditISM_id(audit_id);
        for (Branch auditee : audit.getAuditees()) {
            IS_MGTAuditee.setAuditee_id(auditee.getId());
            auditeeMapper.createISMAuditee(IS_MGTAuditee);
        }

        if (audit.getEdit_auditee()) {
            for (String file_name : audit.getFile_urls()) {
                uploadFileISMMapper.InsertFileUrl(file_name, audit_id);
            }
        }

        for (Change_Tracker_ISM change_Tracker_ISM : audit.getChange_tracker_ISM()) {
            if (change_Tracker_ISM != null) {
                change_Tracker_ISM.setAudit_id(audit_id);
                change_Tracker_ISM.setChanger(audit.getEditor());
                changeTrackerISMService.insertChanges(change_Tracker_ISM);
            }
        }

        recentActivity = new RecentActivity();
        recentActivity.setMessage("Finding with case number  " + audit.getCase_number() + " is created");
        user.setId(user.getId());
        recentActivity.setUser(user);
        recentActivityMapper.addRecentActivity(recentActivity);
    }

    public String generateCaseNumber(String category) {
        int case_number_number = 1;
        String last_case_number = auditeeMapper.getLatestCaseNumber();
        if (last_case_number != null) {
            case_number_number = Integer.parseInt(last_case_number.replaceAll("[^0-9]", ""));
        }
        String case_number = category + (case_number_number + 1);
        return case_number;
    }

    public void updateGeneralObservationFinding(AuditISM audit) {
        User user = userMapper.getAuditorById(audit.getAuditor().getId());
        if (audit.getFinding_detail() == null) {
            geneeralObservationAuditorMapper.updateGeneralObservationFinding(audit);
        } else {
            geneeralObservationAuditorMapper.updateGeneralObservationFindingandFindingDetail(audit);
        }

        if (audit.getEdit_auditee()) {
            commonActionAuditorMapper.deleteISMAuditee(audit.getId());
            IS_MGT_Auditee IS_MGTAuditee = new IS_MGT_Auditee();
            IS_MGTAuditee.setAuditISM_id(audit.getId());

            if (audit.getEdit_auditee()) {
                for (Branch auditee : audit.getAuditees()) {
                    IS_MGTAuditee.setAuditee_id(auditee.getId());
                    auditeeMapper.createISMAuditee(IS_MGTAuditee);
                }
                uploadFileISMMapper.removeFileUrls(audit.getId());
                for (String file_name : audit.getFile_urls()) {
                    uploadFileISMMapper.InsertFileUrl(file_name, audit.getId());
                }
            }

        }

        for (Change_Tracker_ISM change_Tracker_ISM : audit.getChange_tracker_ISM()) {
            if (change_Tracker_ISM != null) {
                change_Tracker_ISM.setAudit_id(audit.getId());
                change_Tracker_ISM.setChanger(audit.getEditor());
                changeTrackerISMService.insertChanges(change_Tracker_ISM);
            }
        }
        recentActivity = new RecentActivity();
        recentActivity.setMessage("Audit with case number  " + audit.getCase_number() + " is updated.");
        user.setId(user.getId());
        recentActivity.setUser(user);
        recentActivityMapper.addRecentActivity(recentActivity);
    }

    public List<AuditISM> getAuditsOnDrafting(PaginatorPayLoad paginatorPayload) {
        // PageHelper.startPage(paginatorPayload.getCurrentPage(), paginatorPayload.getPageSize());
        List<AuditISM> inspectionAudits = geneeralObservationAuditorMapper.getAuditsOnDrafting(paginatorPayload);
        // if (!inspectionAudits.isEmpty()) {
        //     Page<AuditISM> page = (Page<AuditISM>) inspectionAudits;
        //     inspectionAudits.get(0).setTotal_records_paginator(page.getTotal());
        // }
        return inspectionAudits;
    }

    public List<AuditISM> getAuditsOnProgress(PaginatorPayLoad paginatorPayload) {
        // PageHelper.startPage(paginatorPayload.getCurrentPage(), paginatorPayload.getPageSize());
        List<AuditISM> inspectionAudits = geneeralObservationAuditorMapper.getAuditsOnProgress(paginatorPayload);
        // if (!inspectionAudits.isEmpty()) {
        //     Page<AuditISM> page = (Page<AuditISM>) inspectionAudits;
        //     inspectionAudits.get(0).setTotal_records_paginator(page.getTotal());
        // }
        return inspectionAudits;
    }

    public List<AuditISM> getRejectedFindings(PaginatorPayLoad paginatorPayload) {
        // PageHelper.startPage(paginatorPayload.getCurrentPage(), paginatorPayload.getPageSize());
        List<AuditISM> inspectionAudits = geneeralObservationAuditorMapper.getRejectedFindings(paginatorPayload);
        // if (!inspectionAudits.isEmpty()) {
        //     Page<AuditISM> page = (Page<AuditISM>) inspectionAudits;
        //     inspectionAudits.get(0).setTotal_records_paginator(page.getTotal());
        // }
        return inspectionAudits;
    }

    
}
