package com.afr.fms.Common.Report.IS_Report.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.afr.fms.Common.Report.IS_Report.Entity.ISManagementAuditDTO;

@Mapper
public interface ISManagementAuditMapper {

        @Select("<script> DECLARE @branch_id bigint  SET @branch_id = #{branchId}" +
        // " SELECT is_mgt.case_number, dbo._StripHTML(is_mgt.finding) as audit_finding,
        // dbo._StripHTML(is_mgt.recommendation) as audit_recommendation,
        // dbo._StripHTML(is_mgt.impact) as audit_impact, is_mgt.finding_status as
        // finding_status, CAST(is_mgt.rectification_date AS DATE) AS recitified_on, "
                        "  SELECT is_mgt.case_number, is_mgt.target_division_name, ism_auditee.auditee_id as auditee_id, TRIM(b.name) as directorate_name,   is_mgt.finding as audit_finding,   is_mgt.recommendation as audit_recommendation,  is_mgt.impact as audit_impact, COALESCE(ism_auditee.finding_status, is_mgt.finding_status) AS finding_status, "
                        + " CASE WHEN ism_auditee.rectification_status = 1 THEN 'Closed' ELSE 'Open' END AS rectification_status_category, "
                        + " CASE WHEN ism_auditee.rectification_status = 1 THEN 'Rectified' WHEN ism_auditee.rectification_status = 3 OR ism_auditee.rectification_status = 2 THEN 'Unrectified' WHEN ism_auditee.rectification_status = 4 THEN 'Partially Rectified' ELSE 'Unrectified' END as  rectification_status, "
                        +
                        "  CASE WHEN adi.action_plan IS NOT NULL THEN adi.action_plan ELSE 'Pending' END as auditee_response "

                        + "  <choose>  " +
                        "    <when test=\" ( riskLevel != null or  directorateId != null) and singleSelection != null \">  "

                        + " , is_mgt.risk_level as risk_level "

                        + "    </when>  " +
                        "    <when test=\" (startAuditPeriod != null or endAuditPeriod != null ) and singleSelection != null\">  "
                        +
                        "  ,  CAST(is_mgt.finding_date as date ) as finding_identified_on, is_mgt.risk_level as risk_level "

                        + "    </when>  " +
                        "    <when test=\"branchId != null  and singleSelection != null\">  " +
                        " ,  br.name as division_name, is_mgt.risk_level as risk_level "
                        + "    </when>  " +

                        "    <when test=\" (startRectificationDate != null or endRectificationDate != null) and singleSelection != null\">  "

                        + " , is_mgt.risk_level as risk_level "

                        + " , CASE WHEN ism_auditee.rectification_status = 1 or ism_auditee.rectification_status = 4 THEN CAST(CAST(ism_auditee.rectification_date AS DATE) AS VARCHAR)  WHEN ism_auditee.rectification_status = 3 OR ism_auditee.rectification_status = 2 THEN CAST( CAST(ism_auditee.unrectification_date AS DATE) AS VARCHAR)  ELSE 'Pending'  END as recitified_on, "

                        + "    </when>  "

                        + "    <otherwise>  "
                        + " <choose>  "
                        + "  <when test=\"branchId != null\">  "
                        + " , br.name as division_name "
                        + " </when>  "
                        + "  </choose>  "

                        + "  , is_mgt.risk_level as risk_level, CAST(is_mgt.finding_date as date) as finding_identified_on  "
                        + " , CASE WHEN ism_auditee.rectification_status = 1 or ism_auditee.rectification_status = 4 THEN CAST(CAST(ism_auditee.rectification_date AS DATE) AS VARCHAR)  WHEN ism_auditee.rectification_status = 3 OR ism_auditee.rectification_status = 2 THEN CAST( CAST(ism_auditee.unrectification_date AS DATE) AS VARCHAR)  ELSE 'Pending'  END as recitified_on "

                        + "    </otherwise>  " +
                        "  </choose>  " +
                        "  FROM IS_Management_Audit  is_mgt   " +
                        "  INNER JOIN IS_MGT_Auditee ism_auditee ON  is_mgt.id = ism_auditee.IS_MGT_id  " +
                        "  INNER JOIN branch b ON b.id = ism_auditee.auditee_id  " +

                        "  <choose>  " +
                        "    <when test=\"branchId != null \">  " +
                        "  INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = ism_auditee.id  "
                        + "  INNER JOIN branch br ON br.id = adi.division_id  " +
                        "    </when>  " +
                        "    <otherwise>  " +
                        "  LEFT JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = ism_auditee.id  "

                        + "    </otherwise>  " +
                        "  </choose>  " +

                        "  WHERE is_mgt.status = 1 AND is_mgt.category = #{category} " +
                        "  <if test=\"directorateId != null\">  " +
                        "    AND b.id = #{directorateId}  " +
                        "  </if>  " +
                        "  <if test=\"branchId != null\">  " +
                        "    AND br.id = #{branchId}  " +
                        "  </if>  " +
                        "  <if test=\"finding != null\">  " +
                        "    AND  is_mgt.finding = #{finding}  " +
                        "  </if>  " +
                          "  <if test=\"case_number != null\">  " +
                        "    AND  is_mgt.case_number = #{case_number}  " +
                        "  </if>  " +
                        "  <if test=\"riskLevel != null\">  " +
                        "    AND  is_mgt.risk_level = #{riskLevel}  " +
                        "  </if>  " +
                        "  <if test=\"findingStatus != null\">  " +
                        "    <choose>  " +
                        "        <when test=\"findingStatus == 'approved' or findingStatus == 'reviewed'\">  " +
                        "            AND is_mgt.finding_status LIKE '%' + #{findingStatus} + '%'  " +
                        "        </when>  " +
                        "        <otherwise>  " +
                        "            AND ism_auditee.finding_status LIKE '%' + #{findingStatus} + '%'  " +
                        "        </otherwise>  " +
                        "    </choose>  " +
                        "</if> " +
                        "  <if test=\"rectificationStatus != null\">  " +
                        "    AND  ism_auditee.rectification_status = #{rectificationStatus}   " +
                        "  </if>  " +
                        "  <if test=\"startAuditPeriod != null and endAuditPeriod != null\">  " +
                        "    AND  is_mgt.finding_date between #{startAuditPeriod} and #{endAuditPeriod}  " +
                        "  </if>  " +
                        "  <if test=\"startAuditPeriod != null and endAuditPeriod == null\">  " +
                        "    AND  is_mgt.finding_date >= #{startAuditPeriod}  " +
                        "  </if>  " +
                        "  <if test=\"startAuditPeriod == null and endAuditPeriod != null\">  " +
                        "    AND  #{endAuditPeriod}  > = is_mgt.finding_date " +
                        "  </if>  " +

                        "  <if test=\"startRectificationDate != null and endRectificationDate != null\">  " +
                        "    AND  ism_auditee.rectification_date between #{startRectificationDate} and #{endRectificationDate}  "
                        +
                        "  </if>  " +
                        "  <if test=\"startRectificationDate != null and endRectificationDate == null\">  " +
                        "    AND  ism_auditee.rectification_date >= #{startRectificationDate}  " +
                        "  </if>  " +
                        "  <if test=\"startRectificationDate == null and endRectificationDate != null\">  " +
                        "    AND  #{endRectificationDate}  > = ism_auditee.rectification_date " +
                        "  </if>  " +

                        " <choose> " +
                        " <when test=\"role == 'ROLE_AUDITOR_IS'\"> " +
                        " AND is_mgt.auditor_id = #{userId} " +
                        " </when> " +
                        " <when test=\"role == 'ROLE_APPROVER_IS'\"> " +
                        " AND is_mgt.approver_id = #{userId} " +
                        " </when> " +
                        " <when test=\"role == 'ROLE_REVIEWER_IS'\"> " +
                        " AND is_mgt.reviewer_id = #{userId} " +
                        " </when> " +
                        " <when test=\"role == 'ROLE_FOLLOWUP_OFFICER_IS'\"> " +
                        " AND is_mgt.followup_id = #{userId} " +
                        " </when> " +
                        " </choose> " +

                        "</script>  " +
                        "")

        List<ISManagementAuditDTO> searchISManagementAudit(
                        @Param("role") String role,
                        @Param("userId") Long userId,
                        @Param("directorateId") Long directorateId,
                        @Param("branchId") Long branchId,
                        @Param("category") String category,
                        @Param("finding") String finding,
                        @Param("riskLevel") String riskLevel,
                        @Param("findingStatus") String findingStatus,
                        @Param("startAuditPeriod") String startAuditPeriod,
                        @Param("endAuditPeriod") String endAuditPeriod,
                        @Param("singleSelection") String singleSelection,
                        @Param("rectificationStatus") Integer rectificationStatus,
                        @Param("startRectificationDate") String startRectificationDate,
                        @Param("endRectificationDate") String endRectificationDate,
                        @Param("startDraftedDate") String startDraftedDate,
                        @Param("endDraftedDate") String endDraftedDate,
                        @Param("startReviewedDate") String startReviewedDate,
                        @Param("endReviewedDate") String endReviewedDate,
                        @Param("startApprovedDate") String startApprovedDate,
                        @Param("endApprovedDate") String endApprovedDate,
                        @Param("startRespondedDate") String startRespondedDate,
                        @Param("endRespondedDate") String endRespondedDate,  @Param("case_number") String case_number);

}