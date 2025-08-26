package com.afr.fms.Common.Report.MGT_Report.Mapper.NewReport;

import java.util.List;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import com.afr.fms.Common.Report.IS_Report.Entity.ISManagementAuditDTO;
@Mapper
public interface NewManagemetReport {

        @Select("<script> DECLARE @branch_id bigint  SET @branch_id = #{branchId}" +          
        "  SELECT is_mgt.id, is_mgt.case_number, ism_auditee.auditee_id as auditee_id,  TRIM(b.name) as directorate_name, dbo._StripHTML(is_mgt.finding) as audit_finding,  dbo._StripHTML(is_mgt.recommendation) as audit_recommendation, dbo._StripHTML(is_mgt.impact) as audit_impact, COALESCE(ism_auditee.finding_status, is_mgt.finding_status) AS finding_status, "      
        + " CASE WHEN ism_auditee.rectification_status = 1 THEN 'Rectified' WHEN ism_auditee.rectification_status = 3 OR ism_auditee.rectification_status = 2 THEN 'Unrectified' WHEN ism_auditee.rectification_status = 4 THEN 'Partially Rectified' ELSE 'Pending' END as  rectification_status, "
                        + " CASE WHEN ism_auditee.rectification_status = 1 or ism_auditee.rectification_status = 4 THEN CAST(CAST(ism_auditee.rectification_date AS DATE) AS VARCHAR)  WHEN ism_auditee.rectification_status = 3 OR ism_auditee.rectification_status = 2 THEN CAST( CAST(ism_auditee.unrectification_date AS DATE) AS VARCHAR)  ELSE 'Pending'  END as recitified_on, "
                        + " CAST(is_mgt.finding_date as date ) as finding_identified_on, " 
                        + " CAST(adi.response_subimtted_date as date ) as responded_date, " +
                        "  CASE WHEN dbo._StripHTML(adi.action_plan) IS NOT NULL THEN dbo._StripHTML(adi.action_plan) ELSE 'Pending' END as auditee_response "
                        + "  <choose>  " +
                        "    <when test=\" ( finding != null or riskLevel != null  or findingStatus != null  or startRectificationDate != null or endRectificationDate != null  or startAuditPeriod != null or endAuditPeriod != null) and singleSelection != null \">  "  +
                        " , b.name as directorate_name,  is_mgt.amount as amount, is_mgt.fcy as fcy,  is_mgt.cash_type as cash_type "  +
                        "    </when>  " +
                        "    <when test=\"directorateId != null and singleSelection != null\">  " +
                        "  ,  b.name as directorate_name " +
                        "    </when>  " +
                        "    <when test=\"branchId != null  and singleSelection != null\">  " +
                        " ,  br.name as division_name " +
                        "    </when>  " +
                        "    <when test=\" (minAmount != null or maxAmount != null) and singleSelection != null\">  " +
                        " , b.name as directorate_name, CAST(is_mgt.finding_date as date )as finding_identified_on , is_mgt.amount as amount, is_mgt.fcy as fcy,  is_mgt.cash_type as cash_type "

                        + "    </when>  " +

                        "    <otherwise>  " +
                        "   ,  b.name as directorate_name, "
                        + " <choose>  "
                        + "  <when test=\"branchId != null\">  "
                        + " br.name as division_name, "
                        + " </when>  "
                        + "  </choose>  "

                        + " is_mgt.risk_level as risk_level, CAST(is_mgt.finding_date as date) as finding_identified_on, is_mgt.amount as amount, is_mgt.fcy as fcy,  is_mgt.cash_type as cash_type   "
                        +

                        "    </otherwise>  " +
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

                        " <choose> " +
                        "   <when test='audit_type == \"AbnormalBalance\"'> " +
                        "     INNER JOIN abnormal_balance_mgt abm ON abm.is_management_audit_id = is_mgt.id " +
                        "   </when> " +
                        "   <when test='audit_type == \"suspenseAccount\"'> " +
                        "     INNER JOIN suspense_account_mgt sam ON sam.is_mgt_audit_id = is_mgt.id " +
                        "   </when> " +
                        "   <when test='audit_type == \"assetLiability\"'> " +
                        "     INNER JOIN asset_liability_mgt alm ON alm.is_mgt_audit_id = is_mgt.id " +
                        "   </when> " +
                        "   <when test='audit_type == \"memorandomContingent\"'> " +
                        "     INNER JOIN memorandom_contingent_mgt mcg ON mcg.is_mgt_audit_id = is_mgt.id " +
                        "   </when> " +

                        "   <when test='audit_type == \"LongOutstandingItems\"'> " +
                        "     INNER JOIN long_outstanding_item_mgt lig ON lig.mgt_audit_id = is_mgt.id " +
                        "   </when> " +
                        "   <when test='audit_type == \"CashCount\"'> " +
                        "     INNER JOIN cash_count_mgt ccm ON ccm.is_mgt_audit_id = is_mgt.id " +
                        "   </when> " +
                        " </choose> " +
                        "  WHERE is_mgt.status = 1 AND is_mgt.audit_type =#{audit_type} " +
                        "  <if test=\"category != 'GENERAL'\">  " +
                        "    AND is_mgt.category = #{category}   " +
                        "  </if>  " +
                        "  <if test=\"directorateId != null\">  " +
                        "    AND b.id = #{directorateId}  " +
                        "  </if>  " +
                        "  <if test=\"branchId != null\">  " +
                        "    AND br.id = #{branchId}  " +
                        "  </if>  " +
                        "  <if test=\"finding != null\">  " +

                   " AND CONVERT(VARCHAR(MAX), is_mgt.finding) LIKE '%'+#{finding} +'%' "+

                        
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
                        "  " +
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
                        "    AND ism_auditee.rectification_date between #{startRectificationDate} and #{endRectificationDate}  "
                        +
                        "  </if>  " +
                        "  <if test=\"startRectificationDate != null and endRectificationDate == null\">  " +
                        "    AND  ism_auditee.rectification_date >= #{startRectificationDate}  " +
                        "  </if>  " +
                        "  <if test=\"startRectificationDate == null and endRectificationDate != null\">  " +
                        "    AND  #{endRectificationDate}  > = ism_auditee.rectification_date " +
                        "  </if>  " +

                        "  <if test=\"startDraftedDate != null and endDraftedDate != null\">  " +
                        "    AND is_mgt.drafted_date between #{startDraftedDate} and #{endDraftedDate}  " +
                        "  </if>  " +
                        "  <if test=\"startDraftedDate != null and endDraftedDate == null\">  " +
                        "    AND  is_mgt.drafted_date >= #{startDraftedDate}  " +
                        "  </if>  " +
                        "  <if test=\"startDraftedDate == null and endDraftedDate != null\">  " +
                        "    AND  #{endDraftedDate}  > = is_mgt.drafted_date " +
                        "  </if>  " +

                        "  <if test=\"startReviewedDate != null and endReviewedDate != null\">  " +
                        "    AND is_mgt.reviewed_date between #{startReviewedDate} and #{endReviewedDate}  "
                        +
                        "  </if>  " +
                        "  <if test=\"startReviewedDate != null and endReviewedDate == null\">  " +
                        "    AND  is_mgt.reviewed_date >= #{startReviewedDate}  " +
                        "  </if>  " +
                        "  <if test=\"startReviewedDate == null and endReviewedDate != null\">  " +
                        "    AND  #{endReviewedDate}  > = is_mgt.reviewed_date " +
                        "  </if>  " +

                        "  <if test=\"startApprovedDate != null and endApprovedDate != null\">  " +
                        "    AND is_mgt.approved_date between #{startApprovedDate} and #{endApprovedDate}  "
                        +
                        "  </if>  " +
                        "  <if test=\"startApprovedDate != null and endApprovedDate == null\">  " +
                        "    AND  is_mgt.approved_date >= #{startApprovedDate}  " +
                        "  </if>  " +
                        "  <if test=\"startApprovedDate == null and endApprovedDate != null\">  " +
                        "    AND  #{endApprovedDate}  > = is_mgt.approved_date " +
                        "  </if>  " +

                        "  <if test=\"startRespondedDate != null and endRespondedDate != null\">  " +
                        "    AND adi.response_subimtted_date between #{startRespondedDate} and #{endRespondedDate}  "
                        +
                        "  </if>  " +
                        "  <if test=\"startRespondedDate != null and endRespondedDate == null\">  " +
                        "    AND  adi.response_subimtted_date >= #{startRespondedDate}  " +
                        "  </if>  " +
                        "  <if test=\"startRespondedDate == null and endRespondedDate != null\">  " +
                        "    AND  #{startRespondedDate}  > = adi.response_subimtted_date " +
                        "  </if>  " +

                        "  <if test=\"minAmount != null and maxAmount != null \">  " +
                        "    AND  is_mgt.amount between #{minAmount} and #{maxAmount} " +
                        "  </if>  " +

                        "  <if test=\"minAmount != null and maxAmount == null \">  " +
                        "    AND  is_mgt.amount >= #{minAmount}   " +
                        "  </if>  " +

                        "  <if test=\"minAmount == null and maxAmount != null \">  " +
                        "    AND  #{maxAmount} >= is_mgt.amount   " +
                        "  </if>  " +

                        "  <choose>  " +
                        "    <when test=\"role == 'ROLE_AUDITOR_MGT'\">  " +
                        "      AND  is_mgt.auditor_id = #{userId} and is_mgt.category = #{category}  " +
                        "    </when>  " +
                        "    <when test=\"role == 'ROLE_APPROVER_MGT'\">  " +
                        "      AND  is_mgt.approver_id = #{userId} and is_mgt.category = #{category}  " +
                        "    </when>  " +
                        "    <when test=\"role == 'ROLE_REVIEWER_MGT'\">  " +
                        "      AND  is_mgt.reviewer_id = #{userId} and is_mgt.category = #{category} " +
                        "    </when>  " +
                        "    <when test=\"role == 'ROLE_FOLLOWUP_OFFICER_MGT'\">  " +
                        "      AND  is_mgt.followup_id = #{userId} and is_mgt.category = #{category}  " +
                        "    </when>  " +
                        "    <when test=\"role == 'ROLE_AUDITEE'\">  " +
                        "      AND  is_mgt.approve_status = 1  " +
                        "    </when>  " +
                        "  </choose>  " +
                        "</script>  " +
                        "")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "abnormalBalance", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.AbnormalBalanceAuditorMapper.getAbnormalBalanceByManagementId")),
                        @Result(property = "assetAndLiability", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AssetLiabilityAuditorMapper.getAssetAndLiabilityByISMGId")),
                        @Result(property = "suspenseAccount", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.SuspenseAccountAuditorMapper.getSespenseAccoutByISMGTAuditId")),
                        @Result(property = "longOutstandingItems", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.LongOutstandingItemsAuditorMapper.getLongOutstandingItmesByManagementId")),
                        @Result(property = "cashCount", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.CashCountAuditorMapper.getCashCountByManagementId")),
                         @Result(property = "memorandomAndContingent", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.MemorandomContingentAuditorMapper.getMemorandomByByISMGTAuditId")),
        })
        List<ISManagementAuditDTO> searchISManagementAudit(
                        @Param("role") String role,
                        @Param("userId") Long userId,
                        @Param("directorateId") Long directorateId,
                        @Param("branchId") Long branchId,
                        @Param("category") String category,
                        @Param("finding") String finding,
                        @Param("minAmount") Double minAmount,
                        @Param("maxAmount") Double maxAmount,
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
                        @Param("endRespondedDate") String endRespondedDate,
                        @Param("audit_type") String audit_type);
}