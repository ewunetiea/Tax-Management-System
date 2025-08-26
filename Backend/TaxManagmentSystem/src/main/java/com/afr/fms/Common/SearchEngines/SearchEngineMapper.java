package com.afr.fms.Common.SearchEngines;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.*;

import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Auditor.Entity.AuditISM;

@Mapper
public interface SearchEngineMapper {

        @Select("<script>" +
                        "SELECT ism.*, " +
                        "       isma.id AS auditee_id, " +
                        "       isma.auditee_id AS directorate_id, " +
                        "       isma.rectification_status AS auditee_rectification_status, " +
                        "       isma.division_assigned AS auditee_division_assigned, " +
                        "       isma.complete_status AS auditee_responded_added, " +
                        "       isma.rectification_date AS auditee_rectification_date, " +
                        "       isma.unrectification_date AS auditee_unrectification_date, " +
                        "       br.name AS auditee_name, " +
                        "       isma.completed_date AS auditee_responded_date " +
                        "FROM IS_Management_Audit ism " +
                        "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ism.id " +
                        "INNER JOIN Branch br ON br.id = isma.auditee_id " +
                        "WHERE ism.status = 1 " +
                        "  AND ism.auditor_status = 1 " +
                        "  AND ism.review_status = 1 " +

                        "  AND EXISTS ( " +
                        "      SELECT 1 " +
                        "      FROM user_role ur1 " +
                        "      JOIN user_role ur2 ON ur1.role_id = ur2.role_id " +
                        "      WHERE " +

                        "      <choose> " +
                        "        <when test=\"auditISM.reviewer != null\"> " +
                        "          ur1.user_id = ism.reviewer_id " +
                        "          AND ur2.user_id = #{auditISM.reviewer.id} " +
                        "        </when> " +
                        "        <when test=\"auditISM.auditor != null\"> " +
                        "          ur1.user_id = ism.auditor_id " +
                        "          AND ur2.user_id = #{auditISM.auditor.id} " +
                        "        </when> " +
                        "        <when test=\"auditISM.approver != null\"> " +
                        "          ur1.user_id = ism.approver_id " +
                        "          AND ur2.user_id = #{auditISM.approver.id} " +
                        "        </when> " +
                        "      </choose> " +
                        "  ) " +

                        "      <choose> " +
                        "        <when test=\"auditISM.reviewer != null\"> " +
                        "          and ism.approve_status = 1 " +
                        "        </when> " +
                        "        <when test=\"auditISM.approver != null\"> " +
                        "          and ism.approve_status = 1 " +
                        "          and isma.division_assigned = 1 " +
                        "        </when> " +
                        "      </choose> " +

                        "  <if test=\"auditISM.finding != null\"> " +
                        "    AND dbo._StripHTML(ism.finding) LIKE CONCAT('%', dbo._StripHTML(#{auditISM.finding}), '%') "
                        +
                        "  </if> " +

                        "  <if test=\"auditISM.impact != null\"> " +
                        "    AND dbo._StripHTML(ism.impact) LIKE CONCAT('%', dbo._StripHTML(#{auditISM.impact}), '%') "
                        +
                        "  </if> " +

                        "  <if test=\"auditISM.finding_detail != null\"> " +
                        "    AND dbo._StripHTML(ism.finding_detail) LIKE CONCAT('%', dbo._StripHTML(#{auditISM.finding_detail}), '%') "
                        +
                        "  </if> " +

                        "  <if test=\"auditISM.recommendation != null\"> " +
                        "    AND dbo._StripHTML(ism.recommendation) LIKE CONCAT('%', dbo._StripHTML(#{auditISM.recommendation}), '%') "
                        +
                        "  </if> " +

                        "  <if test=\"auditISM.auditees != null and auditISM.auditees.size() > 0\"> " +
                        "    AND isma.auditee_id IN " +
                        "    <foreach item='auditee' collection='auditISM.auditees' open='(' separator=',' close=')'> "
                        +
                        "        #{auditee.id} " +
                        "    </foreach> " +
                        "  </if> " +

                        "  <if test=\"auditISM.auditors != null and auditISM.auditors.size() > 0\"> " +
                        "    AND ism.auditor_id IN " +
                        "    <foreach item='auditor' collection='auditISM.auditors' open='(' separator=',' close=')'> "
                        +
                        "        #{auditor.id} " +
                        "    </foreach> " +
                        "  </if> " +

                        "  <if test=\"auditISM.finding_status != null\"> " +
                        "    AND  (ism.finding_status LIKE CONCAT('%', #{auditISM.finding_status}, '%') or isma.finding_status LIKE CONCAT('%', #{auditISM.finding_status}, '%')) "
                        +
                        "  </if> " +

                        "  <if test=\"auditISM.case_number != null\"> " +
                        "    AND ism.case_number LIKE CONCAT('%', #{auditISM.case_number}, '%') " +
                        "  </if> " +

                        "  <if test=\"auditISM.risk_level != null\"> " +
                        "    AND ism.risk_level = #{auditISM.risk_level} " +
                        "  </if> " +
                        "  <choose> " +
                        "    <when test=\"start_finding_date != null and end_finding_date != null\"> " +
                        "      AND (ism.finding_date BETWEEN #{start_finding_date} AND #{end_finding_date}) " +
                        "    </when> " +
                        "    <when test=\"start_finding_date != null and end_finding_date == null\"> " +
                        "      AND ism.finding_date >= #{start_finding_date} " +
                        "    </when> " +
                        "    <when test=\"start_finding_date == null and end_finding_date != null\"> " +
                        "      AND #{end_finding_date} >= ism.finding_date " +
                        "    </when> " +
                        "  </choose> " +

                        "  <choose> " +
                        "    <when test=\"start_approvement_date != null and end_approvement_date != null\"> " +
                        "      AND (ism.approved_date BETWEEN #{start_approvement_date} AND #{end_approvement_date}) " +
                        "    </when> " +
                        "    <when test=\"start_approvement_date != null and end_approvement_date == null\"> " +
                        "      AND ism.approved_date >= #{start_approvement_date} " +
                        "    </when> " +
                        "    <when test=\"start_approvement_date == null and end_approvement_date != null\"> " +
                        "      AND #{end_approvement_date} >= ism.approved_date " +
                        "    </when> " +
                        "  </choose> " +

                        "  <choose> " +
                        "    <when test=\"start_rectification_date != null and end_rectification_date != null\"> " +
                        "      AND (ism.rectification_date BETWEEN #{start_rectification_date} AND #{end_rectification_date}) "
                        +
                        "    </when> " +
                        "    <when test=\"start_rectification_date != null and end_rectification_date == null\"> " +
                        "      AND ism.rectification_date >= #{start_rectification_date} " +
                        "    </when> " +
                        "    <when test=\"start_rectification_date == null and end_rectification_date != null\"> " +
                        "      AND #{end_rectification_date} >= ism.rectification_date " +
                        "    </when> " +
                        "  </choose> " +

                        "ORDER BY ism.approved_date DESC " +
                        "</script>")

        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
                        @Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
                        @Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
                        @Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
                        @Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
                        @Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

        })
        public List<AuditISM> getProgressFindings(AuditISM auditISM,
                        @Param("start_finding_date") String start_finding_date,
                        @Param("end_finding_date") String end_finding_date,
                        @Param("start_approvement_date") String start_approvement_date,
                        @Param("end_approvement_date") String end_approvement_date,
                        @Param("start_rectification_date") String start_rectification_date,
                        @Param("end_rectification_date") String end_rectification_date);

        @Update("<script>" +
                        "UPDATE IS_Management_Audit SET " +
                        "<choose>" +
                        "  <when test=\"auditISMs[0].role_name.contains('ROLE_AUDITOR')\"> " +
                        "     auditor_id = #{user.id} " +
                        "  </when> " +
                        "  <when test=\"auditISMs[0].role_name.contains('ROLE_REVIEWER')\"> " +
                        "     reviewer_id = #{user.id} " +
                        "  </when> " +
                        "  <when test=\"auditISMs[0].role_name.contains('ROLE_APPROVER')\"> " +
                        "     approver_id = #{user.id} " +
                        "  </when> " +
                        "  <when test=\"auditISMs[0].role_name.contains('ROLE_FOLLOWUP')\"> " +
                        "     followup_id = #{user.id} " +
                        "  </when> " +
                        "</choose>" +
                        " WHERE " +
                        " <if test=\"auditISMs != null and auditISMs.size() > 0\"> " +
                        "     id IN " +
                        "    <foreach item='audit' collection='auditISMs' open='(' separator=',' close=')'> " +
                        "        #{audit.id} " +
                        "    </foreach> " +
                        " </if> " +
                        "</script>")

        public void delegateUsers(List<AuditISM> auditISMs, User user);

        @Select("SELECT CAST(isma.id AS BIGINT) AS id, " +
                        "CAST(CASE WHEN adi.id IS NOT NULL THEN 1 ELSE 0 END AS BIGINT) AS hasDivision " +
                        "FROM IS_Management_Audit ism " +
                        "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ism.id AND isma.auditee_id = #{auditee_id} "
                        +
                        "LEFT JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id " +
                        "WHERE ism.id = #{ism_id}")
        List<Map<String, Long>> getIS_MGT_Auditee(@Param("ism_id") Long ism_id, @Param("auditee_id") Long auditee_id);

        @Delete(" delete from IS_MGT_Auditee where id = #{IS_MGT_Auditee_id} ;"
        // "delete from Audtiee_Division_ISM where IS_MGT_Auditee_id =
        // #{IS_MGT_Auditee_id}"
        )
        public void deleteIS_MGT_Auditee(Long IS_MGT_Auditee_id);

        @Select("<script>" +
                        "SELECT ism.*, " +
                        "       isma.id AS auditee_id, " +
                        "       isma.auditee_id AS directorate_id, " +
                        "       isma.rectification_status AS auditee_rectification_status, " +
                        "       isma.division_assigned AS auditee_division_assigned, " +
                        "       isma.complete_status AS auditee_responded_added, " +
                        "       isma.rectification_date AS auditee_rectification_date, " +
                        "       isma.unrectification_date AS auditee_unrectification_date, " +
                        "       br.name AS auditee_name, " +
                        "        <if test=\"auditISM.role_name != null\"> " +
                        "               CASE " +
                        "               WHEN adi.division_id is NOT NULL THEN br1.name   " +
                        "               ELSE 'Self Response' " +
                        "               END AS division_name, " +
                        "        </if> " +
                        "       isma.completed_date AS auditee_responded_date " +
                        "       FROM IS_Management_Audit ism " +
                        "      <choose> " +
                        "        <when test=\"auditISM.role_name == null\"> " +
                        "               INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ism.id " +
                        "        </when> " +
                        "        <when test=\"auditISM.role_name != null\"> " +
                        "               INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ism.id " +
                        "               INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id and adi.scheduled_date is not null  "
                        +
                        "               LEFT JOIN branch br1 ON br1.id = adi.division_id " +
                        "        </when> " +
                        "      </choose> " +
                        "INNER JOIN Branch br ON br.id = isma.auditee_id " +
                        "WHERE ism.status = 1 " +
                        "  AND ism.auditor_status = 1 " +
                        "  AND ism.review_status = 1 " +
                        "  AND ism.approve_status = 1 " +
                        "  AND isma.rectification_status != 1 " +
                        "  AND isma.rectification_status != 0 " +

                        "      <choose> " +
                        "        <when test=\"auditISM.role_name == null\"> " +
                        "  AND EXISTS ( " +
                        "      SELECT 1 " +
                        "      FROM user_role ur1 " +
                        "      JOIN user_role ur2 ON ur1.role_id = ur2.role_id " +
                        "      WHERE " +

                        "      <choose> " +
                        "        <when test=\"auditISM.reviewer != null\"> " +
                        "          ur1.user_id = ism.reviewer_id " +
                        "          AND ur2.user_id = #{auditISM.reviewer.id} " +
                        "        </when> " +
                        "        <when test=\"auditISM.auditor != null\"> " +
                        "          ur1.user_id = ism.auditor_id " +
                        "          AND ur2.user_id = #{auditISM.auditor.id} " +
                        "        </when> " +
                        "        <when test=\"auditISM.approver != null\"> " +
                        "          ur1.user_id = ism.approver_id " +
                        "          AND ur2.user_id = #{auditISM.approver.id} " +
                        "        </when> " +
                        "      </choose> " +
                        "  ) " +
                        "        </when> " +
                        "        <when test=\"auditISM.role_name != null and auditISM.role_name.contains('ROLE_AUDITEE')\"> "
                        +
                        "        AND isma.auditee_id = #{auditISM.auditee.branch.id} " +
                        "        </when> " +
                        "        <when test=\"auditISM.role_name != null and auditISM.role_name.contains('ROLE_AUDITEE_DIVISION')\"> "
                        +
                        "          AND adi.division_id = #{auditISM.auditee.branch.id} "
                        +
                        "        </when> " +
                        "      </choose> " +

                        "  <if test=\"auditISM.finding != null\"> " +
                        "    AND dbo._StripHTML(ism.finding) LIKE CONCAT('%', dbo._StripHTML(#{auditISM.finding}), '%') "
                        +
                        "  </if> " +

                        "  <if test=\"auditISM.impact != null\"> " +
                        "    AND dbo._StripHTML(ism.impact) LIKE CONCAT('%', dbo._StripHTML(#{auditISM.impact}), '%') "
                        +
                        "  </if> " +

                        "  <if test=\"auditISM.finding_detail != null\"> " +
                        "    AND dbo._StripHTML(ism.finding_detail) LIKE CONCAT('%', dbo._StripHTML(#{auditISM.finding_detail}), '%') "
                        +
                        "  </if> " +

                        "  <if test=\"auditISM.recommendation != null\"> " +
                        "    AND dbo._StripHTML(ism.recommendation) LIKE CONCAT('%', dbo._StripHTML(#{auditISM.recommendation}), '%') "
                        +
                        "  </if> " +

                        "  <if test=\"auditISM.auditees != null and auditISM.auditees.size() > 0\"> " +
                        "    AND isma.auditee_id IN " +
                        "    <foreach item='auditee' collection='auditISM.auditees' open='(' separator=',' close=')'> "
                        +
                        "        #{auditee.id} " +
                        "    </foreach> " +
                        "  </if> " +

                        "  <if test=\"auditISM.auditors != null and auditISM.auditors.size() > 0\"> " +
                        "    AND ism.auditor_id IN " +
                        "    <foreach item='auditor' collection='auditISM.auditors' open='(' separator=',' close=')'> "
                        +
                        "        #{auditor.id} " +
                        "    </foreach> " +
                        "  </if> " +

                        "  <if test=\"auditISM.finding_status != null\"> " +
                        "    AND  (ism.finding_status LIKE CONCAT('%', #{auditISM.finding_status}, '%') or isma.finding_status LIKE CONCAT('%', #{auditISM.finding_status}, '%')) "
                        +
                        "  </if> " +

                        "  <if test=\"auditISM.case_number != null\"> " +
                        "    AND ism.case_number LIKE CONCAT('%', #{auditISM.case_number}, '%') " +
                        "  </if> " +

                        "  <if test=\"auditISM.risk_level != null\"> " +
                        "    AND ism.risk_level = #{auditISM.risk_level} " +
                        "  </if> " +

                        "  <choose> " +
                        "    <when test=\"start_finding_date != null and end_finding_date != null\"> " +
                        "      AND (ism.finding_date BETWEEN #{start_finding_date} AND #{end_finding_date}) " +
                        "    </when> " +
                        "    <when test=\"start_finding_date != null and end_finding_date == null\"> " +
                        "      AND ism.finding_date >= #{start_finding_date} " +
                        "    </when> " +
                        "    <when test=\"start_finding_date == null and end_finding_date != null\"> " +
                        "      AND #{end_finding_date} >= ism.finding_date " +
                        "    </when> " +
                        "  </choose> " +

                        "  <choose> " +
                        "    <when test=\"start_approvement_date != null and end_approvement_date != null\"> " +
                        "      AND (ism.approved_date BETWEEN #{start_approvement_date} AND #{end_approvement_date}) " +
                        "    </when> " +
                        "    <when test=\"start_approvement_date != null and end_approvement_date == null\"> " +
                        "      AND ism.approved_date >= #{start_approvement_date} " +
                        "    </when> " +
                        "    <when test=\"start_approvement_date == null and end_approvement_date != null\"> " +
                        "      AND #{end_approvement_date} >= ism.approved_date " +
                        "    </when> " +
                        "  </choose> " +

                        "  <choose> " +
                        "    <when test=\"start_rectification_date != null and end_rectification_date != null\"> " +
                        "      AND (ism.rectification_date BETWEEN #{start_rectification_date} AND #{end_rectification_date}) "
                        +
                        "    </when> " +
                        "    <when test=\"start_rectification_date != null and end_rectification_date == null\"> " +
                        "      AND ism.rectification_date >= #{start_rectification_date} " +
                        "    </when> " +
                        "    <when test=\"start_rectification_date == null and end_rectification_date != null\"> " +
                        "      AND #{end_rectification_date} >= ism.rectification_date " +
                        "    </when> " +
                        "  </choose> " +

                        "ORDER BY ism.approved_date DESC " +
                        "</script>")

        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
                        @Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
                        @Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
                        @Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
                        @Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
                        @Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
        })
        public List<AuditISM> getScheduleRectificationFindings(AuditISM auditISM,
                        @Param("start_finding_date") String start_finding_date,
                        @Param("end_finding_date") String end_finding_date,
                        @Param("start_approvement_date") String start_approvement_date,
                        @Param("end_approvement_date") String end_approvement_date,
                        @Param("start_rectification_date") String start_rectification_date,
                        @Param("end_rectification_date") String end_rectification_date);

}
