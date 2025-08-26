package com.afr.fms.Auditee.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Auditee.Entity.AuditeeDivisionFileISM;
import com.afr.fms.Auditee.Entity.AuditeeDivisionISM;
import com.afr.fms.Auditee.Mapper.AuditeeDivisionISMMapper;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Entity.IS_MGT_Auditee;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;
import com.afr.fms.Followup_officer.Mapper.FollowupOfficerISMMapper;

@Service
public class AuditeeISMService {
    @Autowired
    private AuditeeDivisionISMMapper auditDivisionISMMapper;

    @Autowired
    private FollowupOfficerISMMapper followupOfficerISMMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RecentActivityMapper recentActivityMapper;
    @Autowired
    private AuditeeService auditeeService;

    RecentActivity recentActivity = new RecentActivity();

    private static final Logger logger = LoggerFactory.getLogger(AuditeeISMService.class);

    public List<AuditeeDivisionISM> getAuditeeResponses(Long IS_MGT_Auditee_id) {
        return auditDivisionISMMapper.getAuditeeResponse(IS_MGT_Auditee_id);
    }

    public AuditeeDivisionISM getAuditeeResponseISM(Long auditee_division_id) {
        return auditDivisionISMMapper.getAuditeeResponseISM(auditee_division_id);
    }

    public void add_response(AuditeeDivisionISM auditeeDivisionISM) {
        Long auditee_division_id = auditeeDivisionISM.getId();

        if (auditeeDivisionISM.getSelf_response() == 1) {
            auditee_division_id = auditDivisionISMMapper.add_self_response(auditeeDivisionISM);
        } else {
            if (auditeeDivisionISM.getRectification_status() > 0) {
                updatePreviousResponse(auditeeDivisionISM);
            }
            auditDivisionISMMapper.add_response(auditeeDivisionISM);
        }

        if (auditeeDivisionISM.getFile_flag() && !auditeeDivisionISM.getAttached_files().getFile_urls().isEmpty()) {
            auditDivisionISMMapper.deleteAttachedFiles(auditee_division_id);
            for (String file_url : auditeeDivisionISM.getAttached_files().getFile_urls()) {
                auditDivisionISMMapper.add_attached_files(auditee_division_id, file_url);
            }
        }

        if (auditeeDivisionISM.getSelf_response() != 1 && auditeeDivisionISM.getSelf_response() != 2) {
            auditDivisionISMMapper.submitResponseStatus(auditee_division_id);
        }

        if (auditeeDivisionISM.getRectification_status() > 0) {
            auditeeDivisionISM.setId(auditee_division_id);
            handleRectificationRelatedActions(auditeeDivisionISM);
        }
    }

    public void handleRectificationRelatedActions(AuditeeDivisionISM auditeeDivisionISM) {
        try {

            AuditISM auditISM = new AuditISM();
            String action_type = "";
            User followupOfficer = new User();
            IS_MGT_Auditee is_MGT_Auditee = new IS_MGT_Auditee();

            auditISM.setId(auditeeDivisionISM.getAudit_id());
            followupOfficer.setId(auditeeDivisionISM.getResponder_id());
            is_MGT_Auditee.setId(auditeeDivisionISM.getIS_MGT_Auditee_id());
            auditISM.setFollowup_officer(followupOfficer);
            auditISM.setAuditee_id(auditeeDivisionISM.getIS_MGT_Auditee_id());
            auditISM.setDivision_auditee_id(auditeeDivisionISM.getId());

            auditeeService.finishAuditeeResponse(auditISM);

            if (auditeeDivisionISM.getRectification_status() == 4) {
                action_type = "Partially Rectified";
                followupOfficerISMMapper.partiallyRectifyISMFindings(auditISM);
                followupOfficerISMMapper.partiallyRectifyFindingsAuditee(is_MGT_Auditee);
                followupOfficerISMMapper.updateIS_MGT_Auditee(is_MGT_Auditee.getId());
                followupOfficerISMMapper.updateAuditeeDivision(auditeeDivisionISM.getSelf_response(),
                        auditeeDivisionISM);

            } else if (auditeeDivisionISM.getRectification_status() == 1) {
                action_type = "Rectified";
                followupOfficerISMMapper.rectifyISMFindings(auditISM);
                followupOfficerISMMapper.rectifyFindingsAuditee(is_MGT_Auditee);
            }

            else if (auditeeDivisionISM.getRectification_status() == 2) {
                action_type = "Unrectified";
                followupOfficerISMMapper.rejectISMFinding(auditISM);
                followupOfficerISMMapper.unrectifyFindingAuditee(is_MGT_Auditee);
                followupOfficerISMMapper.updateIS_MGT_Auditee(is_MGT_Auditee.getId());
                followupOfficerISMMapper.updateAuditeeDivision(auditeeDivisionISM.getSelf_response(),
                        auditeeDivisionISM);
            }

            // AuditISM commonAuditISM =
            // auditDivisionISMMapper.getCommonAuditByID(auditISM.getId());
            recentActivity.setMessage(" Finding " + auditeeDivisionISM.getCase_number() + " is " + action_type);
            recentActivity.setUser(followupOfficer);
            recentActivityMapper.addRecentActivity(recentActivity);
        } catch (Exception e) {
            logger.error("Error while handling rectification related actions", e);
        }
    }

    public void updatePreviousResponse(AuditeeDivisionISM auditeeDivisionISM) {
        try {
            Long auditee_division_id = auditeeDivisionISM.getId();

            AuditeeDivisionISM auditeeDivisionISM2 = auditDivisionISMMapper.getAuditeeResponseISM(auditee_division_id);
            auditDivisionISMMapper.update_previous_response(auditeeDivisionISM2);
            auditDivisionISMMapper.deletePreviouslyAttachedFiles(auditee_division_id);

            if (auditeeDivisionISM2.getUploaded_files() != null
                    && !auditeeDivisionISM2.getUploaded_files().isEmpty()) {
                for (AuditeeDivisionFileISM auditeeDivisionFileISM : auditeeDivisionISM2.getUploaded_files()) {
                    auditDivisionISMMapper.add_previously_attached_files(auditee_division_id,
                            auditeeDivisionFileISM.getFile_url());
                }
            }
        } catch (Exception e) {
            logger.error("Error while updating previous response", e);
        }

    }

    // @DreamerAba(Abebayehu Alaro)
    // Algorithm to allow division manager to look only its response.
    public List<AuditISM> retrieveResponseForDivision(List<AuditISM> auditISMs, Long division_id) {
        List<AuditISM> audits = new ArrayList<>();
        for (AuditISM auditISM : auditISMs) {

            // possible result is one auditee or directorate. In case auditee inputs its
            // divisions wrongly, the error will be generated. To controll it, list used.
            List<IS_MGT_Auditee> auditees = new ArrayList<>();
            List<IS_MGT_Auditee> is_MGT_Auditees = auditDivisionISMMapper
                    .getISMAuditeeByAuditandDivisionID(auditISM.getId(), division_id);
            for (IS_MGT_Auditee is_MGT_Auditee : is_MGT_Auditees) {

                // List used for the same reason as the above auditee.
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

    public List<AuditISM> getAuditsForAuditeeDivision(Long division_id) {
        User user = userMapper.getAuditorById(division_id);

        List<AuditISM> auditISMs = auditDivisionISMMapper.getAuditsForAuditeeDivision(user.getBranch().getId());

        return retrieveResponseForDivision(auditISMs, user.getBranch().getId());
    }

    public List<AuditISM> getPartiallyRectifiedAuditsForAuditeeDivision(Long division_id) {
        User user = userMapper.getAuditorById(division_id);

        List<AuditISM> auditISMs = auditDivisionISMMapper
                .getPartiallyRectifiedAuditsForAuditeeDivision(user.getBranch().getId());

        return retrieveResponseForDivision(auditISMs, user.getBranch().getId());
    }

    public List<AuditISM> getAuditsForAuditeeDivisionOnProgress(Long division_id) {
        User user = userMapper.getAuditorById(division_id);

        List<AuditISM> auditISMs = auditDivisionISMMapper
                .getAuditsForAuditeeDivisionOnProgress(user.getBranch().getId());

        return retrieveResponseForDivision(auditISMs, user.getBranch().getId());
    }

    public List<AuditISM> getUnrectifiedAuditsForAuditeeDivision(Long division_id) {
        User user = userMapper.getAuditorById(division_id);

        List<AuditISM> auditISMs = auditDivisionISMMapper
                .getUnrectifiedAuditsForAuditeeDivision(user.getBranch().getId());

        return retrieveResponseForDivision(auditISMs, user.getBranch().getId());
    }

    public void submitISMFinding(AuditISM audit) {
        auditDivisionISMMapper
                .submitResponsetoAuditee(audit.getIS_MGTAuditee().get(0).getAuditeeDivisionISM().get(0).getId());
        // auditDivisionISMMapper.updateDivisionResponseStatus(audit.getId());
    }

    public void clearDuplicateISMFinding(AuditISM audit) {
        try {
            clearDivisionMapping(audit);
        } catch (Exception e) {
            logger.error("Error while clearing division mapping for duplicate ISM findings", e);
        }

        try {
            clearAuditeeMapping(audit);
        } catch (Exception e) {
            logger.error("Error while clearing auditee mapping for duplicate ISM findings", e);
        }
    }

    public void clearDivisionMapping(AuditISM audit) {
        List<Long> divisionIdsToDelete = audit.getAuditeeDivisionISM().stream()
                .filter(division -> division.getAction_plan() == null)
                .map(AuditeeDivisionISM::getId)
                .collect(Collectors.toList());

        if (!divisionIdsToDelete.isEmpty()) {
            auditDivisionISMMapper.clearDivisionMappingDuplicateFindings(divisionIdsToDelete);
        }

    }

    public void clearAuditeeMapping(AuditISM audit) {

        Long division_id = audit.getAuditeeDivisionISM().get(0).getDivision().getId();

        List<Long> auditeeIdsToDelete = audit.getIS_MGTAuditee().stream()
                .map(IS_MGT_Auditee::getId)
                .collect(Collectors.toList());

        for (Long auditeeId : auditeeIdsToDelete) {
            int count = auditDivisionISMMapper.countAuditeeOtherDivision(division_id, auditeeId);
            if (count == 0) {
                auditDivisionISMMapper.clearAuditeeMappingDuplicateFindings(auditeeId);
            }
        }

    }

}
