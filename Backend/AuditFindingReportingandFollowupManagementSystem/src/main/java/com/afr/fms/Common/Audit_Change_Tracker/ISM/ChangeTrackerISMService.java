package com.afr.fms.Common.Audit_Change_Tracker.ISM;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChangeTrackerISMService {
    @Autowired
    private ChangeTrackerISMMapper changeTrackerISMMapper;

    public void insertChanges(Change_Tracker_ISM change_Tracker_ISM) {
        changeTrackerISMMapper.insertChange(change_Tracker_ISM);
    }
    public void insertChangeINS(Change_Tracker_ISM change_Tracker_ISM) {
        changeTrackerISMMapper.insertChangeINS(change_Tracker_ISM);
    }
    

   
    public List<Change_Tracker_ISM> getChanges(Long audit_id) {
        return changeTrackerISMMapper.getChanges(audit_id);
    }
}
