package com.afr.fms.Payload.realtime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RealTime {
	private Long id;
	private Long last_notified_loan;
	private Long last_notification;
	private Long last_notification_admin;
	private Long user_id;
	private Long region_id;
	private Boolean is_saved;
	private Boolean approver_approve;
	private Boolean approver_reject;
	private Boolean maker_edit; 
	private Boolean maker_disburse;
	private Boolean notify;
	private Boolean isAdmin;
}
