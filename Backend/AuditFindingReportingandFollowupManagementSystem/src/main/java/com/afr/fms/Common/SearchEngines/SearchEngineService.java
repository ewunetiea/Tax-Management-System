package com.afr.fms.Common.SearchEngines;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Entity.Branch;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Auditee.Service.AuditeeISMService;
import com.afr.fms.Auditee.Service.AuditeeService;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Entity.IS_MGT_Auditee;
import com.afr.fms.Auditor.Mapper.AuditISMMapper;
import com.afr.fms.Auditor.Service.AuditISMService;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchEngineService {

    @Autowired
    private SearchEngineMapper searchEngineMapper;
    @Autowired
    private AuditeeISMService auditeeDivisionISMService;

    @Autowired
    private AuditISMService auditISMService;

    @Autowired
    private AuditeeService auditeeService;

    @Autowired
    private AuditISMMapper auditMapper;

    @Autowired
    private RecentActivityMapper recentActivityMapper;

    RecentActivity recentActivity = new RecentActivity();

    private static final Logger logger = LoggerFactory.getLogger(SearchEngineService.class);

    public List<AuditISM> getProgressFindings(AuditISM auditISM) {

        PageHelper.startPage(auditISM.getPage_number(), auditISM.getPage_size());
        Page<AuditISM> auditsPage = (Page<AuditISM>) searchEngineMapper.getProgressFindings(auditISM,
                checkRequistionDates(auditISM.getFinding_dates(), 0) ? auditISM.getFinding_dates().get(0)
                        : null,
                checkRequistionDates(auditISM.getFinding_dates(), 1) ? auditISM.getFinding_dates().get(1)
                        : null,
                checkRequistionDates(auditISM.getApproved_dates(), 0) ? auditISM.getApproved_dates().get(0)
                        : null,
                checkRequistionDates(auditISM.getApproved_dates(), 1) ? auditISM.getApproved_dates().get(1)
                        : null,
                checkRequistionDates(auditISM.getRectification_dates(), 0) ? auditISM.getRectification_dates().get(0)
                        : null,
                checkRequistionDates(auditISM.getRectification_dates(), 1) ? auditISM.getRectification_dates().get(1)
                        : null);
        List<AuditISM> auditISMs = auditsPage.getResult(); // Get the actual list of audits from the page object
        if (auditISMs.size() > 0) {
            auditISMs.get(0).setTotal_records(auditsPage.getTotal());
        }

        return auditISMService.attachAuditeeResponse(auditISMs);
    }

    public List<AuditISM> getScheduleRectificationFindings(AuditISM auditISM) {
        PageHelper.startPage(auditISM.getPage_number(), auditISM.getPage_size());
        Page<AuditISM> auditsPage = (Page<AuditISM>) searchEngineMapper.getScheduleRectificationFindings(auditISM,
                checkRequistionDates(auditISM.getFinding_dates(), 0) ? auditISM.getFinding_dates().get(0)
                        : null,
                checkRequistionDates(auditISM.getFinding_dates(), 1) ? auditISM.getFinding_dates().get(1)
                        : null,
                checkRequistionDates(auditISM.getApproved_dates(), 0) ? auditISM.getApproved_dates().get(0)
                        : null,
                checkRequistionDates(auditISM.getApproved_dates(), 1) ? auditISM.getApproved_dates().get(1)
                        : null,
                checkRequistionDates(auditISM.getRectification_dates(), 0) ? auditISM.getRectification_dates().get(0)
                        : null,
                checkRequistionDates(auditISM.getRectification_dates(), 1) ? auditISM.getRectification_dates().get(1)
                        : null);

        List<AuditISM> auditISMs = auditsPage.getResult(); // Get the actual list of audits from the page object
        List<AuditISM> audits = new ArrayList<>();
        if (auditISM.getRole_name() == null) {
            audits = auditISMService.attachScheduledAuditeeResponse(auditISMs);
        } else if (auditISM.getRole_name().equals("ROLE_AUDITEE_DIVISION")) {
            audits = auditeeDivisionISMService.retrieveResponseForDivision(auditISMs,
                    auditISM.getAuditee().getBranch().getId());
        } else if (auditISM.getRole_name().equals("ROLE_AUDITEE")) {
            audits = auditeeService.attachAuditeeResponse(auditISMs, auditISM.getAuditee().getBranch().getId());
        }
        if (audits != null && audits.size() > 0) {
            audits.get(0).setTotal_records(auditsPage.getTotal());
        }
        return audits;
    }

    public boolean checkRequistionDates(List<String> finding_dates, int index) {
        if (finding_dates != null) {
            if (index == 0)
                return finding_dates.size() > 0;
            return finding_dates.size() > 1;
        }
        return false;
    }

    public void delegateUsers(List<AuditISM> audits) {

        try {
            User user = audits.get(0).getAuditors().get(0);
            searchEngineMapper.delegateUsers(audits, user);
            try {
                String allCaseNumbers = audits.stream()
                        .map(AuditISM::getCase_number)
                        .collect(Collectors.joining(", "));

                recentActivity.setMessage(
                        " Findings " + allCaseNumbers + " role " + audits.get(0).getRole_name()
                                + " users/user is delegated");
                recentActivity.setUser(audits.get(0).getEditor());
                recentActivityMapper.addRecentActivity(recentActivity);
            } catch (Exception e) {
                logger.error("Error while adding recent activity: {}", e.getMessage());
            }
        } catch (Exception e) {
            logger.error("Error while delegating users: {}", e.getMessage());
        }

    }

    public void manageAuditees(AuditISM auditISM) {
        try {
            List<Branch> auditees = auditISM.getAuditees();

            List<IS_MGT_Auditee> is_MGT_Auditees = auditMapper.getISMAuditees(auditISM.getId());

            List<Branch> oldAuditees = is_MGT_Auditees.stream()
                    .map(IS_MGT_Auditee::getAuditee)
                    .collect(Collectors.toList());

            List<Branch> uniqueDivisionsDB = auditeeService.returnUniqueDivisions(oldAuditees,
                    auditees, true);

            List<Branch> uniqueDivisionsNew = auditeeService.returnUniqueDivisions(oldAuditees,
                    auditees, false);

            for (Branch auditee : uniqueDivisionsDB) {
                List<Map<String, Long>> results = searchEngineMapper.getIS_MGT_Auditee(auditISM.getId(),
                        auditee.getId());
                if (!results.isEmpty()) {
                    Map<String, Long> result = results.get(0);
                    Long id = result.get("id");
                    Long hasDivision = result.get("hasDivision");
                    if (hasDivision != 1) {
                        searchEngineMapper.deleteIS_MGT_Auditee(id);
                    }
                }

            }

            IS_MGT_Auditee IS_MGTAuditee = new IS_MGT_Auditee();
            IS_MGTAuditee.setAuditISM_id(auditISM.getId());
            for (Branch auditee : uniqueDivisionsNew) {
                IS_MGTAuditee.setAuditee_id(auditee.getId());
                auditMapper.createISMAuditee(IS_MGTAuditee);
            }

            try {
                recentActivity.setMessage(" Finding " + auditISM.getCase_number() + " auditee assignment is amended.");
                recentActivity.setUser(auditISM.getEditor());
                recentActivityMapper.addRecentActivity(recentActivity);
            } catch (Exception e) {
                logger.error("Error while adding recent activity: {}", e.getMessage());
            }

        } catch (

        Exception e) {
            logger.error("Error while amending auditee assignment: {}", e.getMessage());
        }

    }

}
