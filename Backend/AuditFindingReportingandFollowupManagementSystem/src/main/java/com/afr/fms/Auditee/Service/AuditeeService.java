package com.afr.fms.Auditee.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Entity.Branch;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Auditee.Entity.AuditeeDivisionISM;
import com.afr.fms.Auditee.Mapper.AuditeeISMMapper;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Entity.IS_MGT_Auditee;
import com.afr.fms.Auditor.Mapper.AuditISMMapper;
import com.afr.fms.Common.Audit_Remark.Remark;
import com.afr.fms.Common.Audit_Remark.RemarkService;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;
import com.afr.fms.Payload.endpoint.Endpoint;
import com.afr.fms.Security.email.context.Auditee_for_DivisionContext;
import com.afr.fms.Security.email.context.Auditee_for_FollowupOfficerContext;
import com.afr.fms.Security.email.service.EmailService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class AuditeeService {
    @Autowired
    private AuditeeISMMapper auditDivisionISMMapper;

    @Autowired
    private AuditISMMapper auditISMMapper;

    @Autowired
    private UserMapper userMapper;
    private String baseURL = Endpoint.URL;
    @Autowired
    private EmailService emailService;

    @Autowired
    private RemarkService remarkService;

    @Autowired
    private RecentActivityMapper recentActivityMapper;

    RecentActivity recentActivity = new RecentActivity();

    private static final Logger logger = LoggerFactory.getLogger(AuditeeService.class);

    public List<AuditeeDivisionISM> getAuditeeResponses(Long IS_MGT_Auditee_id) {
        return auditDivisionISMMapper.getAuditeeResponse(IS_MGT_Auditee_id);
    }

    public AuditeeDivisionISM getAuditeeResponseISM(Long auditee_division_id) {
        return auditDivisionISMMapper.getAuditeeResponseISM(auditee_division_id);
    }

    public void add_auditee_division_ism(IS_MGT_Auditee IS_MGTAuditeeOriginal) {
        for (AuditISM auditISM : IS_MGTAuditeeOriginal.getAudits()) {
            if (auditISM.getIS_MGTAuditee() == null || auditISM.getIS_MGTAuditee().isEmpty()) {
                continue;
            }

            IS_MGT_Auditee IS_MGTAuditeeEmail = new IS_MGT_Auditee();
            IS_MGTAuditeeEmail.setAssigner(IS_MGTAuditeeOriginal.getAssigner());
            IS_MGTAuditeeEmail.setAuditISM(auditISM);
            IS_MGT_Auditee IS_MGTAuditee = auditISM.getIS_MGTAuditee().get(0);
            IS_MGTAuditee.setAuditISM_id(auditISM.getId());
            IS_MGTAuditee.setDivisions(IS_MGTAuditeeOriginal.getDivisions());
            Long auditee_id = auditISM.getIS_MGTAuditee().get(0).getId();
            List<AuditeeDivisionISM> assignedAuditeeDivisions = auditDivisionISMMapper
                    .getAssignedAuditeeDivisions(auditee_id);
            List<Branch> divisions = assignedAuditeeDivisions.stream()
                    .map(AuditeeDivisionISM::getDivision)
                    .collect(Collectors.toList());
            List<Branch> uniqueDivisionsDB = returnUniqueDivisions(divisions, IS_MGTAuditee.getDivisions(), true);
            List<Branch> uniqueDivisionsNew = returnUniqueDivisions(divisions, IS_MGTAuditee.getDivisions(), false);
            if (!divisions.equals(IS_MGTAuditee.getDivisions())) {
                for (Branch branch : uniqueDivisionsDB) {
                    AuditeeDivisionISM auditeeDivision = assignedAuditeeDivisions.stream()
                            .filter(ad -> ad.getDivision().getId().equals(branch.getId()))
                            .findFirst()
                            .orElse(null);
                    if (auditeeDivision.getAction_plan() == null) {
                        auditDivisionISMMapper.delete_auditee_division_ism(branch.getId(),
                                auditeeDivision.getIS_MGT_Auditee_id());
                    }
                }

                IS_MGTAuditeeEmail.setDivisions(uniqueDivisionsNew);
                for (Branch division : uniqueDivisionsNew) {
                    auditDivisionISMMapper.add_auditee_division_ism(division.getId(),
                            auditee_id);
                }
                auditDivisionISMMapper.updateDivisionAssignmentAuditee(IS_MGTAuditee.getId());
                if (IS_MGTAuditeeOriginal.isSent_email()) {
                    try {
                        sendEmailtoAuditeeDivision(IS_MGTAuditeeEmail);
                        System.out.println("Email is sent to Division");
                    } catch (Exception e) {
                        logger.error("Error while sending email to auditee division", e);
                    }

                }

            }
        }
    }

    public List<Branch> returnUniqueDivisions(List<Branch> assignedDivisionsDB, List<Branch> newAssignedDivisions,
            boolean flag) {
        // boolean flag; use to identify from which list of divisions, I want to retun
        // unique
        // divisions.
        if (flag) {

            List<Branch> commonDivisions = new ArrayList<>(assignedDivisionsDB);

            commonDivisions.retainAll(newAssignedDivisions);

            List<Branch> uniqueDivisions = new ArrayList<>(assignedDivisionsDB);

            uniqueDivisions.removeAll(commonDivisions);

            return uniqueDivisions;
        } else {
            List<Branch> commonDivisions = new ArrayList<>(newAssignedDivisions);

            commonDivisions.retainAll(assignedDivisionsDB);

            List<Branch> uniqueDivisions = new ArrayList<>(newAssignedDivisions);

            uniqueDivisions.removeAll(commonDivisions);

            return uniqueDivisions;
        }
    }

    public List<AuditISM> getAuditsForAuditee(AuditISM auditISM) {
        User user = userMapper.getAuditorById(auditISM.getAuditee_id());
        auditISM.setAuditee_id(user.getBranch().getId());
        PageHelper.startPage(auditISM.getPage_number(), auditISM.getPage_size());
        Page<AuditISM> auditsPage = (Page<AuditISM>) auditDivisionISMMapper.getAuditsForAuditee(auditISM,
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
        return attachAuditeeResponse(auditISMs, user.getBranch().getId());

    }

    public boolean checkRequistionDates(List<String> finding_dates, int index) {
        if (finding_dates != null) {
            if (index == 0)
                return finding_dates.size() > 0;
            return finding_dates.size() > 1;
        }
        return false;
    }

    // @DreamerAba(Abebayehu Alaro)
    // Algorithm to allow director to look only its divisions' response.

    public List<AuditISM> attachAuditeeResponse(List<AuditISM> auditISMs, Long auditee_id) {
        List<AuditISM> audits = new ArrayList<>();
        for (AuditISM auditISM : auditISMs) {
            List<Branch> assignedDivisions = new ArrayList<>();

            List<IS_MGT_Auditee> is_MGT_Auditees = auditDivisionISMMapper.getISMAuditeesByID(auditISM.getAuditee_id());
            List<AuditeeDivisionISM> assignedDivisionsInfo = auditDivisionISMMapper
                    .getAuditeeResponseById(auditISM.getDivision_auditee_id());

            assignedDivisions = getAssignedDivisions(is_MGT_Auditees.get(0).getId());
            is_MGT_Auditees.get(0).setDivisions(assignedDivisions);

            is_MGT_Auditees.get(0).setAuditeeDivisionISM(assignedDivisionsInfo);

            is_MGT_Auditees.get(0).setAssignedAuditeeDivisionISM(assignedDivisionsInfo);
            auditISM.setIS_MGTAuditee(is_MGT_Auditees);
            audits.add(auditISM);
        }
        return audits;
    }

    public List<Branch> getAssignedDivisions(Long auditee_id) {
        List<AuditeeDivisionISM> assignedAuditeeDivisions = auditDivisionISMMapper
                .getAssignedAuditeeDivisions(auditee_id);

        List<Branch> divisions = assignedAuditeeDivisions.stream()
                .map(AuditeeDivisionISM::getDivision)
                .collect(Collectors.toList());
        return divisions;
    }

    public List<AuditISM> getPartiallyRectifiedAuditsForAuditee(Long auditee_id) {
        User user = userMapper.getAuditorById(auditee_id);
        List<AuditISM> auditISMs = auditDivisionISMMapper
                .getPartiallyRectifiedAuditsForAuditee(user.getBranch().getId());
        return attachAuditeeResponse(auditISMs, user.getBranch().getId());
    }

    public List<AuditISM> getAuditsForAuditeeOnProgress(Long auditee_id) {
        User user = userMapper.getAuditorById(auditee_id);
        List<AuditISM> auditISMs = auditDivisionISMMapper.getAuditsForAuditeeOnProgress(user.getBranch().getId());
        return attachAuditeeResponse(auditISMs, user.getBranch().getId());
    }

    public List<AuditISM> getUnrectifiedAuditsForAuditee(Long auditee_id) {
        User user = userMapper.getAuditorById(auditee_id);
        List<AuditISM> auditISMs = auditDivisionISMMapper.getUnrectifiedAuditsForAuditee(user.getBranch().getId());
        return attachAuditeeResponse(auditISMs, user.getBranch().getId());
    }

    public List<AuditISM> getRespondedAuditsForAuditee(AuditISM auditISM) {
        User user = userMapper.getAuditorById(auditISM.getAuditee_id());
        auditISM.setAuditee_id(user.getBranch().getId());
        PageHelper.startPage(auditISM.getPage_number(), auditISM.getPage_size());
        Page<AuditISM> auditsPage = (Page<AuditISM>) auditDivisionISMMapper.getRespondedAuditsForAuditee(auditISM,
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

        return attachAuditeeResponse(auditISMs, user.getBranch().getId());
    }

    public void finishAuditeeResponse(AuditISM auditISM) {
        auditDivisionISMMapper.finishAuditeeResponse(auditISM.getAuditee_id());

        auditDivisionISMMapper.submitAuditAuditeeResponse(auditISM.getDivision_auditee_id());
    }

    public void submitSelectedAuditeeResponse(List<AuditISM> audits) {
        for (AuditISM auditISM : audits) {
            // for (IS_MGT_Auditee is_MGT_Auditee : auditISM.getIS_MGTAuditee()) {
            auditDivisionISMMapper.finishAuditeeResponse(auditISM.getAuditee_id());
            // }

            auditDivisionISMMapper.submitAuditAuditeeResponse(auditISM.getDivision_auditee_id());
        }

        if (audits.get(0).getIs_email()) {
            User auditee = userMapper.getAuditorById(audits.get(0).getAuditee_id());
            AuditISM auditInfo = auditISMMapper.getAudit(audits.get(0).getId());
            List<User> user = new ArrayList<>();
            user.add(auditInfo.getAuditor());
            user.add(auditee);
            auditInfo.setUsers(user);
            try {
                sendEmailtoFollowupOfficer(auditInfo);
            } catch (Exception e) {
                logger.error("Error while sending email to followup officer", e);
            }
        }

    }

    public void rejectFindings(Remark remark) {
        remark.setRejected(false);
        List<AuditISM> auditISMs = remark.getAudits();
        User followupUser = auditISMs.get(0).getFollowup_officer();
        for (AuditISM auditISM : auditISMs) {
            auditISM.setFollowup_officer(followupUser);
            remark.setAudit(auditISM);
            AuditISM auditISMInfo = auditISMMapper.getAudit(auditISM.getId());
            IS_MGT_Auditee IS_MGTAuditee = auditISM.getIS_MGTAuditee().get(0);
            IS_MGTAuditee.setAuditee_rejected(1);
            IS_MGTAuditee.setRejector(followupUser);
            auditDivisionISMMapper.rejectFindings(IS_MGTAuditee);
            try {
                List<User> recievers = new ArrayList<>();
                recievers.add(auditISMInfo.getAuditor());
                recievers.add(auditISMInfo.getReviewer());
                recievers.add(auditISMInfo.getApprover());
                for (User reciever : recievers) {
                    remark.setReciever(reciever);
                    remarkService.addRemark(remark);
                }
            } catch (Exception e) {
                logger.error("Error while sending remark to actors", e);
            }
            recentActivity.setMessage(" Finding " + auditISM.getCase_number() + " is rejected.");
            recentActivity.setUser(followupUser);
            recentActivityMapper.addRecentActivity(recentActivity);
        }
    }

    public void sendEmailtoFollowupOfficer(AuditISM auditISM) {
        Auditee_for_FollowupOfficerContext emailContext = new Auditee_for_FollowupOfficerContext();
        emailContext.init(auditISM);
        emailContext.buildFollowupOfficerUrl(baseURL);
        try {
            emailService.sendMail(emailContext);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendEmailtoAuditeeDivision(IS_MGT_Auditee is_MGT_Auditee) {
        AuditISM auditISM = is_MGT_Auditee.getAuditISM();
        auditISM.setAuditee(is_MGT_Auditee.getAssigner());
        for (Branch division : is_MGT_Auditee.getDivisions()) {
            try {
                List<User> recieversDivision = userMapper
                        .getUserByBranchandRole(division.getId(),
                                "AUDITEE_DIVISION");
                for (User reciever : recieversDivision) {
                    auditISM.setEditor(reciever);
                    Auditee_for_DivisionContext emailContext = new Auditee_for_DivisionContext();
                    emailContext.init(auditISM);
                    emailContext.buildDivisionUrl(baseURL);
                    try {
                        emailService.sendMail(emailContext);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                logger.error("Error while sending email to auditee division", e);
            }
        }

    }

}
