package com.afr.fms.Common.FraudCase;

import java.util.List;

import org.apache.ibatis.annotations.*;

@Mapper
public interface FraudCaseMapper {

	@Insert("insert into fraud_case(format, user_id, initial, created_date,case_type,fraud_status) values(#{format}, #{user.id}, 0,CURRENT_TIMESTAMP,#{case_type}, 0)")
	public void createFraudCase(FraudCase fraudCase);

	@Select("select * from fraud_case where initial=#{initial}")
	public FraudCase getInitialFraudCase(Long initial);

	@Select("select * from fraud_case where id=#{id}")
	public FraudCase getFraudCaseById(Long id);

	@Update("update fraud_case set format=#{format}, user_id=#{user.id}, initial=#{initial} , updated_date=CURRENT_TIMESTAMP where id=#{id}")
	public void updateFraudCase(FraudCase fraudCase);

	@Update("update fraud_case set fraud_status=3, approver_id=#{approver.id} where id=#{id}")
	public void approveFraudCase(FraudCase fraudCase);

	@Update("update fraud_case set fraud_status=1 where id=#{id}")
	public void cancelApprovedFraudCase(Long id);

	@Select("select approver_id from fraud_case where id=#{id}")
	public Long getApproverId(Long id);

	@Select("select user_id from fraud_case where id=#{id}")
	public Long getAuditorId(Long id);

	@Select("select * from fraud_case where initial != 1 and user_id=#{auditor_id} and fraud_status=0 ")
	public List<FraudCase> getDraftedFraudAuditor(Long auditor_id);

	@Select("select * from fraud_case where initial != 1 and user_id=#{auditor_id} and fraud_status=1")
	public List<FraudCase> getPassedFraudAuditor(Long auditor_id);

	@Select("select * from fraud_case where initial != 1 and user_id=#{auditor_id} and fraud_status=3")
	public List<FraudCase> getApprovedFraudAuditor(Long auditor_id);

	@Select("select * from fraud_case where fraud_status =1 and initial !=1 ")
	public List<FraudCase> getPendingFraudCasesApprover();

	@Select("select * from fraud_case where approver_id= #{approver_id} and fraud_status=3 and initial !=1")
	public List<FraudCase> getApprovedFraudCasesApprover(Long approver_id);

	@Delete("delete from fraud_case where id=#{id}")
	public void deleteFraudCase(Long id);

	@Delete("update  fraud_case set fraud_status=1 where id=#{id}")
	public void passFraudCase(Long id);

	@Delete("update  fraud_case set fraud_status=0 where id=#{id}")
	public void backFraudCase(Long id);

}
