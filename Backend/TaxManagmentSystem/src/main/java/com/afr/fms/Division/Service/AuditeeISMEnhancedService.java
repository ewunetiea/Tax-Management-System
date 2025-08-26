package com.afr.fms.Division.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.mail.MessagingException;
import org.springframework.stereotype.Service;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Auditee.Entity.AuditeeDivisionISM;
import com.afr.fms.Auditee.Mapper.AuditeeDivisionISMEnhancedMapper;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Entity.IS_MGT_Auditee;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import com.afr.fms.Common.Permission.Service.FunctionalitiesService;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;
import com.afr.fms.Payload.endpoint.Endpoint;
import com.afr.fms.Security.email.context.Division_for_AuditeeContext;
import com.afr.fms.Security.email.service.EmailService;

@Service
public class AuditeeISMEnhancedService {
    @Autowired
    private AuditeeDivisionISMEnhancedMapper auditDivisionISMMapper;

    @Autowired
    private UserMapper userMapper;

    RecentActivity recentActivity = new RecentActivity();

    @Autowired
    private RecentActivityMapper recentActivityMapper;

    ;

    private String baseURL = Endpoint.URL;

    @Autowired
    private FunctionalitiesService functionalitiesService;

    @Autowired
    private EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(AuditeeISMEnhancedService.class);

    public List<AuditeeDivisionISM> getAuditeeResponses(Long IS_MGT_Auditee_id) {
        return auditDivisionISMMapper.getAuditeeResponse(IS_MGT_Auditee_id);
    }

    public AuditeeDivisionISM getAuditeeResponseISM(Long auditee_division_id) {
        return auditDivisionISMMapper.getAuditeeResponseISM(auditee_division_id);
    }

    // Algorithm to allow division manager to look only its response.
    public List<AuditISM> retrieveResponseForDivision(List<AuditISM> auditISMs, Long division_id) {
        List<AuditISM> audits = new ArrayList<>();
        for (AuditISM auditISM : auditISMs) {
            List<IS_MGT_Auditee> auditees = new ArrayList<>();
            List<IS_MGT_Auditee> is_MGT_Auditees = auditDivisionISMMapper
                    .getISMAuditeeByAuditandDivisionID(auditISM.getId(), division_id);
            for (IS_MGT_Auditee is_MGT_Auditee : is_MGT_Auditees) {
                List<AuditeeDivisionISM> auditeeDivisionISM = auditDivisionISMMapper
                        .getAuditeeResponseByAuditeeandDivisionID(division_id, is_MGT_Auditee.getId());
                is_MGT_Auditee.setAuditeeDivisionISM(auditeeDivisionISM);
                auditees.add(is_MGT_Auditee);
            }
            auditISM.setIS_MGTAuditee(auditees);
            audits.add(auditISM);
        }
        return audits;
    }

    public List<AuditISM> getRespondedAuditsForAuditeeDivision(Long division_id) {
        User user = userMapper.getAuditorById(division_id);
        List<AuditISM> auditISMs = auditDivisionISMMapper
                .getRespondedAuditsForAuditeeDivision(user.getBranch().getId());
        return retrieveResponseForDivision(auditISMs, user.getBranch().getId());
    }

    public List<AuditISM> getAuditsForAuditeeDivision(PaginatorPayLoad paginatorPayload) {
        User user = userMapper.getAuditorById(paginatorPayload.getUser_id());
        // PageHelper.startPage(paginatorPayload.getCurrentPage(),
        // paginatorPayload.getPageSize());
        List<AuditISM> auditISMs = auditDivisionISMMapper.getAuditsForAuditeeDivision(user.getBranch().getId(),
                paginatorPayload.getAudit_type());
        // if (!auditISMs.isEmpty()) {
        // Page<AuditISM> page = (Page<AuditISM>) auditISMs;
        // auditISMs.get(0).setTotal_records_paginator(page.getTotal());
        // }
        return retrieveResponseForDivision(auditISMs, user.getBranch().getId());
    }

    public List<AuditISM> getPartiallyRectifiedAuditsForAuditeeDivision(PaginatorPayLoad paginatorPayLoad) {
        User user = userMapper.getAuditorById(paginatorPayLoad.getUser_id());
        List<AuditISM> auditISMs = auditDivisionISMMapper.getPartiallyRectifiedAuditsForAuditeeDivision(
                user.getBranch().getId(), paginatorPayLoad.getAudit_type());
        return retrieveResponseForDivision(auditISMs, user.getBranch().getId());
    }

    public List<AuditISM> getAuditsForAuditeeDivisionOnProgress(PaginatorPayLoad paginatorPayload) {
        User user = userMapper.getAuditorById(paginatorPayload.getUser_id());
        // PageHelper.startPage(paginatorPayload.getCurrentPage(),
        // paginatorPayload.getPageSize());
        List<AuditISM> auditISMs = auditDivisionISMMapper
                .getAuditsForAuditeeDivisionOnProgress(user.getBranch().getId(), paginatorPayload.getAudit_type());
        // if (!auditISMs.isEmpty()) {
        // Page<AuditISM> page = (Page<AuditISM>) auditISMs;
        // auditISMs.get(0).setTotal_records_paginator(page.getTotal());
        // }
        return retrieveResponseForDivision(auditISMs, user.getBranch().getId());
    }

    public List<AuditISM> getUnrectifiedAuditsForAuditeeDivision(PaginatorPayLoad paginatorPayLoad) {
        User user = userMapper.getAuditorById(paginatorPayLoad.getUser_id());
        List<AuditISM> auditISMs = auditDivisionISMMapper
                .getUnrectifiedAuditsForAuditeeDivision(user.getBranch().getId(), paginatorPayLoad.getAudit_type());
        return retrieveResponseForDivision(auditISMs, user.getBranch().getId());
    }

    public void submitISMFinding(AuditISM audit) {
        auditDivisionISMMapper
                .submitResponsetoAuditee(audit.getIS_MGTAuditee().get(0).getAuditeeDivisionISM().get(0).getId());
        // auditDivisionISMMapper.updateDivisionResponseStatus(audit.getId());
    }

    public void submitAndEmailSelectedISMFinding(List<AuditISM> audits, HttpServletRequest request) {
        for (AuditISM audit : audits) {
            auditDivisionISMMapper
                    .submitResponsetoAuditee(audit.getIS_MGTAuditee().get(0).getAuditeeDivisionISM().get(0).getId());

            User user = functionalitiesService.getUserFromHttpRequest(request);
            recentActivity.setMessage(" Auditee Response for audit " + audit.getCase_number() + " is submitted");
            recentActivity.setUser(user);
            recentActivityMapper.addRecentActivity(recentActivity);
        }

        if (audits.get(0).getIs_email()) {
            try {
                sendEmailFromDivisionToAuditee(audits);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public List<User> filterUniqueUsers(List<User> users) {
        List<User> uniqueUsers = users.stream()
                .collect(Collectors.toMap(User::getId, u -> u, (oldUser, newUser) -> oldUser))
                .values().stream()
                .collect(Collectors.toList());
        return uniqueUsers;
    }

    public void sendEmailFromDivisionToAuditee(List<AuditISM> audits) {
        String division_name = audits.get(0).getDivision_name();
        AuditISM audit = new AuditISM();
        Map<Long, List<String>> directorateToCaseNumbers = audits.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        AuditISM::getDirectorate_id,
                        Collectors.mapping(AuditISM::getCase_number, Collectors.toList())));

        for (Map.Entry<Long, List<String>> entry : directorateToCaseNumbers.entrySet()) {
            Long directorateId = entry.getKey();
            String joinedCaseNumbers = String.join(", ", entry.getValue());
            List<User> users = new ArrayList<>();
            users.addAll(userMapper.getUserByBranchandRole(directorateId, "AUDITEE"));
            audit.setDirectorate_id(directorateId);
            audit.setCase_number(joinedCaseNumbers);
            Division_for_AuditeeContext emailContext = new Division_for_AuditeeContext();
            audit.setDivision_name(division_name);
            for (User user : users) {
                audit.setAuditee(user);
                emailContext.init(audit);
                emailContext.buildDisbursmentUrl(baseURL);
                try {
                    emailService.sendMail(emailContext);
                    logger.error("Email sent to : ", user.getEmail());
                } catch (MessagingException e) {
                    logger.error("Error sending email to : ", user.getEmail());
                    e.printStackTrace();
                }

            }
        }
    }
}
