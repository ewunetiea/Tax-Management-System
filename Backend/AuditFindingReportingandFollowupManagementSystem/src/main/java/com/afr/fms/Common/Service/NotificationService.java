package com.afr.fms.Common.Service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Payload.realtime.RealTimeMapper;

import java.lang.Long;

@Service
public class NotificationService {

	@Autowired
	private UserMapper userMapper;

	// @Autowired
	// private NotificationMapper notificationMapper;

	// @Autowired
	// private RealTimeMapper realTimeMapper;

	// public List<Loan> notify(RealTime realtime) {
	// 	List<Long> loan_user_id_list = loanMapper.getLoanUserIdByUserId(realtime.getUser_id());
	// 	List<Long> loan_user_id_listN = notificationMapper.getLoanUserId();

	// 	List<Long> loan_user_list_un = new ArrayList<>();
	// 	for (Long id : loan_user_id_list) {
	// 		for (Long idN : loan_user_id_listN) {
	// 			if (Long.compare(id, idN) == 0) {
	// 				loan_user_list_un.add(id);
	// 				break;
	// 			}
	// 		}
	// 	}
	// 	List<Long> loan_id_list = new ArrayList<>();
	// 	for (Long id : loan_user_list_un) {
	// 		loan_id_list.add(loanMapper.getLoanIdByLoanUserId(id));
	// 	}

	// 	List<Loan> loan_list = new ArrayList<>();
	// 	for (Long id : loan_id_list) {
	// 		loan_list.add(loanMapper.getLoanById(id));
	// 	}
	// 	// return loan_list;

	// 	// List<Loan> loan_list2 = loan_list;
	// 	if (!loan_list.isEmpty()) {
	// 		Long notification_id = loan_list.get(0).getId();
	// 		if (realtime.getIs_saved()) {
	// 			if (realTimeMapper.retrieveRealTimeInfoNotification(realtime) == null) {
	// 				realtime.setLast_notification(notification_id);
	// 				;
	// 				realTimeMapper.insertLastNotification(realtime);
	// 			} else {
	// 				Long last_notification_id = realTimeMapper.retrieveRealTimeInfoNotification(realtime)
	// 						.getLast_notification();
	// 				if (Long.compare(last_notification_id, notification_id) != 0) {
	// 					realtime.setLast_notification(last_notification_id);
	// 					realTimeMapper.updateRealTimeInfoNotification(realtime);
	// 				}
	// 			}
	// 		} else {
	// 			if (Long.compare(notification_id, realtime.getLast_notification()) == 0) {
	// 				return null;
	// 			} else {
	// 				realtime.setLast_notification(notification_id);
	// 				realTimeMapper.updateRealTimeInfoNotification(realtime);
	// 			}
	// 		}
	// 		return loan_list;
	// 	}
	// 	return new ArrayList<>();
	// }

}
