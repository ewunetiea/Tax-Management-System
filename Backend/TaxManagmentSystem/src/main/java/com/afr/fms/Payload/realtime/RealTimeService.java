package com.afr.fms.Payload.realtime;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RealTimeService {

    @Autowired
    private RealTimeMapper realTimeMapper;

    public RealTime getRealTimeByUserId(RealTime realTime) {
        if (realTime.getApprover_approve()) {
            return realTimeMapper.retrieveRealTimeInfoApproverApprove(realTime);
        } else if (realTime.getApprover_reject()) {
            return realTimeMapper.retrieveRealTimeInfoApproverReject(realTime);
        } else if (realTime.getMaker_disburse()) {
            return realTimeMapper.retrieveRealTimeInfoMakerDisburse(realTime);
        } else if (realTime.getMaker_edit()) {
            return realTimeMapper.retrieveRealTimeInfoMakerEdit(realTime);
        } else if (realTime.getNotify()) {
            return realTimeMapper.retrieveRealTimeInfoNotification(realTime);
        } else if (realTime.getIsAdmin()) {
            return realTimeMapper.retrieveRealTimeInfoNotifyAdmin(realTime);
        } else {
            return new RealTime();
        }
    }

    public void insertRealTimeInfo(RealTime realTime) {
        if (realTime.getLast_notification() != -1) {
            realTimeMapper.insertLastNotification(realTime);
        } else if (realTime.getLast_notification_admin() != -1) {
            realTimeMapper.insertLastNotificationAdmin(realTime);

        } else {
            realTimeMapper.insertLastNotifiedLoanApprove(realTime);
        }
    }

    // public void updateRealTimeInfo(RealTime realTime) {
    // realTimeMapper.updateRealTimeInfo(realTime);
    // }
}
