package com.afr.fms.Payload.realtime;

import org.apache.ibatis.annotations.*;

@Mapper
public interface RealTimeMapper {

	@Update("update real_time set last_notified_loan = #{last_notified_loan} where user_id = #{user_id} and approver_approve = 1")
	public void updateRealTimeInfoApproverApprove(RealTime realTime);

	@Update("update real_time set last_notified_loan = #{last_notified_loan} where user_id = #{user_id} and maker_disburse = 1")
	public void updateRealTimeInfoMakerDisburse(RealTime realTime);

	@Update("update real_time set last_notified_loan = #{last_notified_loan} where user_id = #{user_id} and maker_edit = 1")
	public void updateRealTimeInfoMakerEdit(RealTime realTime);

	@Update("update real_time set last_notified_loan = #{last_notified_loan} where user_id = #{user_id} and approver_reject = 1")
	public void updateRealTimeInfoApproverReject(RealTime realTime);

	@Update("update real_time set last_notification = #{last_notification} where user_id = #{user_id} and notify = 1")
	public void updateRealTimeInfoNotification(RealTime realTime);

	@Update("update real_time set last_notification_admin = #{last_notification_admin} where user_id = #{user_id}")
	public void updateRealTimeInfoAdminNotification(RealTime realTime);

	@Insert("insert into real_time (last_notification, user_id, notify) values (#{last_notification}, #{user_id}, 1)")
	public void insertLastNotification(RealTime realTime);

	@Insert("insert into real_time (last_notification_admin, user_id) values (#{last_notification_admin}, #{user_id})")
	public void insertLastNotificationAdmin(RealTime realTime);

	@Insert("insert into real_time (last_notified_loan, user_id, approver_approve) values (#{last_notified_loan}, #{user_id},1)")
	public void insertLastNotifiedLoanApprove(RealTime realTime);

	@Insert("insert into real_time (last_notified_loan, user_id, approver_reject) values (#{last_notified_loan}, #{user_id},1)")
	public void insertLastNotifiedLoanReject(RealTime realTime);

	@Insert("insert into real_time (last_notified_loan, user_id, maker_disburse) values (#{last_notified_loan}, #{user_id},1)")
	public void insertLastNotifiedLoanDisburse(RealTime realTime);

	@Insert("insert into real_time (last_notified_loan, user_id, maker_edit) values (#{last_notified_loan}, #{user_id},1)")
	public void insertLastNotifiedLoanEdit(RealTime realTime);

	@Select("select * from real_time where user_id  = #{user_id} and approver_approve = 1")
	public RealTime retrieveRealTimeInfoApproverApprove(RealTime realTime);

	@Select("select * from real_time where user_id  = #{user_id}  and approver_reject = 1")
	public RealTime retrieveRealTimeInfoApproverReject(RealTime realTime);

	@Select("select * from real_time where user_id  = #{user_id} and maker_disburse = 1")
	public RealTime retrieveRealTimeInfoMakerDisburse(RealTime realTime);

	@Select("select * from real_time where user_id  = #{user_id}  and maker_edit = 1 ORDER BY id DESC")
	public RealTime retrieveRealTimeInfoMakerEdit(RealTime realTime);

	
	@Select("select * from real_time where user_id  = #{user_id}  and notify = 1 ORDER BY id DESC")
	public RealTime retrieveRealTimeInfoNotification(RealTime realTime);
	
	@Select("select * from real_time where user_id  = #{user_id} ORDER BY id DESC")
	public RealTime retrieveRealTimeInfoNotifyAdmin(RealTime realTime);

}
