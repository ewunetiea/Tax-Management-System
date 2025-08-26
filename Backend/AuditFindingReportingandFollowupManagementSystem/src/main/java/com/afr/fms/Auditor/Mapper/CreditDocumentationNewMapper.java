package com.afr.fms.Auditor.Mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Model.MGT.CreditDocumentationChild;
import com.afr.fms.Model.MGT.CreditDocumentationParent;

@Mapper
public interface CreditDocumentationNewMapper {

        @Select("INSERT INTO credit_documentation_parent_mgt (" +
                        "is_mgt_audit_id, borrowerName, arrearsAmount, fees_and_charges, fee_charge_collected, fee_charge_uncollected, "
                        +
                        "interest_income, interest_income_collected, interest_income_uncollected, loanGrantedDate, loanSettlementDate, collateral_type, "
                        +
                        "value_of_collateral, penalty_charge, penality_charge_collected, penality_charge_uncollected, cash_type, fcy, category ,"
                        +
                        "loanInterestRate, one_percent_stamp_duty_charge, one_percent_stamp_duty_charge_collected, one_percent_stamp_duty_charge_uncollected "
                        +
                        ") " +
                        "OUTPUT inserted.id " +
                        "VALUES (" +
                        "#{is_mgt_audit_id}, #{borrowerName}, #{arrearsAmount}, #{fees_and_charges}, #{fee_charge_collected}, #{fee_charge_uncollected}, "
                        +
                        "#{interest_income}, #{interest_income_collected}, #{interest_income_uncollected}, CAST(#{loanGrantedDate} AS DATE), CAST(#{loanSettlementDate} AS DATE), "
                        +
                        "#{collateral_type}, #{value_of_collateral}, #{penalty_charge}, #{penality_charge_collected}, #{penality_charge_uncollected}, "
                        +
                        "#{cash_type}, #{fcy}, #{category} , #{loanInterestRate}, #{one_percent_stamp_duty_charge}, #{one_percent_stamp_duty_charge_collected}, "
                        +
                        "#{one_percent_stamp_duty_charge_uncollected}" +
                        ")")

        public Long createCreditDocumentationParent(CreditDocumentationParent creditDocumentationParent);

        @Select("SELECT * FROM credit_documentation_parent_mgt WHERE is_mgt_audit_id = #{is_mgt_audit_id}")
        public List<CreditDocumentationParent> getcreditDocumentationByISMGId(Long is_mgt_audit_id);

        @Update("UPDATE credit_documentation_parent_mgt " +
                        "SET " +
                        "borrowerName = #{borrowerName}, " +
                        "loanGrantedDate = CAST(#{loanGrantedDate} AS DATE)," +
                        "loanSettlementDate = CAST(#{loanSettlementDate} AS DATE)," +
                        "loanInterestRate = #{loanInterestRate}, " +
                        "arrearsAmount = #{arrearsAmount}, " +
                        "fees_and_charges = #{fees_and_charges}, " +
                        "fee_charge_collected = #{fee_charge_collected}, " +
                        "fee_charge_uncollected = #{fee_charge_uncollected}, " +
                        "interest_income = #{interest_income}, " +
                        "interest_income_collected = #{interest_income_collected}, " +
                        "interest_income_uncollected = #{interest_income_uncollected}, " +
                        "penalty_charge = #{penalty_charge}, " +
                        "penality_charge_collected = #{penality_charge_collected}, " +
                        "penality_charge_uncollected = #{penality_charge_uncollected}, " +
                        "collateral_type = #{collateral_type}, " +
                        "value_of_collateral = #{value_of_collateral}, " +
                        "cash_type = #{cash_type}, " +
                        "fcy = #{fcy}, " +
                        "one_percent_stamp_duty_charge = #{one_percent_stamp_duty_charge}, " +
                        "one_percent_stamp_duty_charge_collected = #{one_percent_stamp_duty_charge_collected}, " +
                        "one_percent_stamp_duty_charge_uncollected = #{one_percent_stamp_duty_charge_uncollected} " +
                        "WHERE is_mgt_audit_id = #{is_mgt_audit_id}")
        public void updateCreditDocumentationParent(CreditDocumentationParent creditDocumentationParent);

        @Select("SELECT * FROM credit_documentation_parent_mgt WHERE is_mgt_audit_id = #{is_mgt_audit_id}")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "creditDocumentationChild", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.CreditDocumentationNewMapper.getCreditDocumentationChildByParentId"))
        })
        public CreditDocumentationParent getCreditDocumentationParentByISMGTID(Long is_mgt_audit_id);

        @Select("select * from  credit_documentation_child_mgt where  credit_documentation_parent_id = #{id}")
        @Results(value = {
                        @Result(property = "id", column = "id"),
        })
        public List<CreditDocumentationChild> getCreditDocumentationChildByParentId(Long id);

        @SelectProvider(type = SqlProvider.class, method = "getLoanAndAdvanceQuery")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
                        @Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
                        @Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
                        @Result(property = "creditDocumentationParent", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.CreditDocumentationNewMapper.getCreditDocumentationParentByISMGTID")),
                        @Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
                        @Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
                        @Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
        })

        public List<AuditISM> getFindingBasedOnStatus(@Param("user_id") Long user_id, @Param("status") String status,
                        @Param("category") String category, @Param("banking") String banking);

        class SqlProvider {
                public static String getLoanAndAdvanceQuery(Map<String, Object> params) {
                        String status = (String) params.get("status");
                        String category = (String) params.get("category");
                        Long user_id = params.get("user_id") != null ? Long.valueOf(params.get("user_id").toString())
                                        : null;
                        String banking = (String) params.get("banking");

                        if ("drafted".equals(status)) {
                                // Query for "drafted" status
                                return "select * from IS_Management_Audit " +
                                                "where id in (select is_mgt_audit_id from credit_documentation_parent_mgt) "
                                                +
                                                "and auditor_id = #{user_id} and auditor_status != 1 and status = 1 " +
                                                "Order By drafted_date DESC";
                        }

                        else if ("passed".equals(status)) {
                                return "select * from IS_Management_Audit " +
                                                "where id in (select is_mgt_audit_id from credit_documentation_parent_mgt) "
                                                +
                                                "and auditor_id = #{user_id} and auditor_status = 1 " +
                                                "and review_status != 1 and status = 1 " +
                                                "Order By passed_date DESC";
                        }

                        else if ("rejected".equals(status)) {

                                return "select * from IS_Management_Audit " +
                                                "where id in (select is_mgt_audit_id from credit_documentation_parent_mgt) "
                                                +
                                                " and auditor_id = #{user_id} and auditor_status = 1 " +
                                                " and status = 1  and review_status = 2 " +
                                                "Order By reviewer_rejected_date  DESC";
                        }

                        else if ("pending".equals(status)) {

                                // Query for "pending" status
                                return "select ida.*   from IS_Management_Audit ida " +

                                                "where ida.id in (select is_mgt_audit_id from credit_documentation_parent_mgt) "
                                                +
                                                "and ida.auditor_status = 1 and ida.status = 1 and ida.review_status != 1 "
                                                +
                                                "and category = #{category}  and management = #{banking} " +
                                                "Order By passed_date DESC";
                        }

                        else if ("reviewed".equals(status)) {
                                return "select * from IS_Management_Audit " +
                                                "where id in (select is_mgt_audit_id from credit_documentation_parent_mgt) "
                                                +
                                                "and reviewer_id = #{user_id} " +
                                                "and auditor_status = 1 " +
                                                "and status = 1 " +
                                                "and review_status = 1 " +
                                                "and approve_status != 1 " +
                                                "Order By reviewed_date DESC";
                        }

                        else if ("approver-commented".equals(status)) {

                                System.out.println("______________From approver rejected finding");
                                return "select * from IS_Management_Audit " +
                                                "where id in (select is_mgt_audit_id from credit_documentation_parent_mgt) "
                                                +
                                                "and reviewer_id = #{user_id} " +
                                                "and auditor_status = 1 " +
                                                "and status = 1 " +
                                                "and review_status = 1 " +
                                                "and approve_status = 2  Order By approver_rejected_date DESC ";
                        }

                        else if ("pending-approver".equals(status)) {
                                return "select * from IS_Management_Audit " +
                                                "where id in (select is_mgt_audit_id from credit_documentation_parent_mgt) "
                                                +
                                                " and category = #{category} and review_status = 1 " +
                                                "and status = 1 " +
                                                "and review_status = 1 " +
                                                " and approve_status = 0  and status = 1  Order By reviewed_date DESC";
                        }

                        else if ("approved".equals(status)) {
                                return "SELECT ida.* " +
                                                "FROM IS_Management_Audit ida " +
                                                "INNER JOIN credit_documentation_parent_mgt cei ON cei.is_mgt_audit_id = ida.id "
                                                +
                                                "LEFT JOIN inspection_auditee ia ON ia.inspection_id = ida.id " +
                                                "WHERE ida.approver_id = #{user_id} " +
                                                "AND ida.status = 1 and  ida.approver_id= #{user_id} and ida.approve_status=1 "
                                                +
                                                // "AND ia.division_assigned = 0 " +
                                                // "AND ia.self_response = 0 " +
                                                // "AND ida.approve_status = 1 " +
                                                "ORDER BY approved_date DESC";
                        }

                        else if ("approver-cancel".equals(status)) {
                                return "SELECT ida.* " +
                                                "FROM IS_Management_Audit ida " +
                                                "INNER JOIN credit_documentation_parent_mgt cei ON cei.is_mgt_audit_id = ida.id "
                                                +
                                                "LEFT JOIN inspection_auditee ia ON ia.inspection_id = ida.id " +
                                                "WHERE ida.approver_id = #{user_id} " +
                                                "AND ida.status = 1 and  ida.approver_id= #{user_id} and ida.approve_status= 2 "
                                                +
                                                // "AND ia.division_assigned = 0 " +
                                                // "AND ia.self_response = 0 " +
                                                // "AND ida.approve_status = 1 " +
                                                "ORDER BY approver_rejected_date DESC";
                        }

                        // Default case for unsupported statuses (if any)
                        throw new IllegalArgumentException("Unsupported status: " + status);
                }
        }

        @Insert(" insert into credit_documentation_child_mgt(loanType , category ,loanAmount , accountNumber, outstandingBalance, loanRepaymentAndInstallment, repayment_amount, credit_documentation_parent_id, fees_and_charges, fee_charge_collected , fee_charge_uncollected,  fee_commision_type, fee_commision_category, finding, finding_detail, impact, recommendation) values (#{loanType} , #{category} ,#{loanAmount} , #{accountNumber}  , #{outstandingBalance}  , #{loanRepaymentAndInstallment}  , #{repayment_amount}  , #{credit_documentation_parent_id} , #{fees_and_charges} , #{fee_charge_collected} , #{fee_charge_uncollected},  #{fee_commision_type} , #{fee_commision_category}, COALESCE(#{finding}, ''), COALESCE(#{finding_detail}, ''), COALESCE(#{impact}, ''), COALESCE(#{recommendation}, '')) ")
        public Long createCreditDocumentationChild(CreditDocumentationChild loanAdvanceCommon);

        @Update(" update credit_documentation_child_mgt set loanType = #{loanType} , category = #{category} , loanAmount = #{loanAmount}, accountNumber = #{accountNumber}, outstandingBalance = #{outstandingBalance}, loanRepaymentAndInstallment = #{loanRepaymentAndInstallment}, repayment_amount = #{repayment_amount},   finding = COALESCE(#{finding}, ''),\r\n"
                        + //
                        "        finding_detail = COALESCE(#{finding_detail}, ''),\r\n" + //
                        "        impact = COALESCE(#{impact}, ''),\r\n" + //
                        "        recommendation = COALESCE(#{recommendation}, '') where  id = #{id} ")
        public void updateCreditDocumentationChild(CreditDocumentationChild loanAdvanceCommon);

        @Select("SELECT CASE WHEN EXISTS (SELECT 1 FROM credit_documentation_child_mgt WHERE accountNumber = #{account_number}) THEN 1 ELSE 0 END")
        public boolean getAccountNumberExistance(String account_number);
}
