package com.afr.fms.Common.FraudCase;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;

@Service
public class FraudCaseService {
    @Autowired
    private FraudCaseMapper fraudCaseMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RecentActivityMapper recentActivityMapper;
    private RecentActivity recentActivity;

    public void createFraudCase(FraudCase fraudCase) {

        fraudCaseMapper.createFraudCase(fraudCase);

        User user = userMapper.getAuditorById(fraudCase.getUser().getId());

        recentActivity = new RecentActivity();
        recentActivity.setMessage("Fraud Case type " + fraudCase.getCase_type() + " is added.");
        user.setId(user.getId());
        recentActivity.setUser(user);
        recentActivityMapper.addRecentActivity(recentActivity);
    }

    public void updateFraudCase(FraudCase fraudcase) {
        fraudCaseMapper.updateFraudCase(fraudcase);
        User user = userMapper.getAuditorById(fraudcase.getUser().getId());

        recentActivity = new RecentActivity();
        recentActivity.setMessage("Fraud Case type " + fraudcase.getCase_type() + " is updated.");

        user.setId(user.getId());
        recentActivity.setUser(user);
        recentActivityMapper.addRecentActivity(recentActivity);

    }

    public FraudCase getInitialFraudCase(Long initial) {
        return fraudCaseMapper.getInitialFraudCase(initial);
    }

    public List<FraudCase> getDraftedFraudAuditor(Long user_id) {
        return fraudCaseMapper.getDraftedFraudAuditor(user_id);
    }

    public List<FraudCase> getPassedFraudAuditor(Long user_id) {
        return fraudCaseMapper.getPassedFraudAuditor(user_id);
    }

    public List<FraudCase> getApprovedFraudAuditor(Long user_id) {
        return fraudCaseMapper.getApprovedFraudAuditor(user_id);
    }

    public void passFraudCase(Long id) {
        fraudCaseMapper.passFraudCase(id);

        FraudCase fraudcase = fraudCaseMapper.getFraudCaseById(id);
        Long auditor_id = fraudCaseMapper.getAuditorId(id);
        User user = userMapper.getAuditorById(auditor_id);
        recentActivity = new RecentActivity();
        recentActivity.setMessage("Fraud Case type " + fraudcase.getCase_type() + " is passed to the approver.");
        user.setId(user.getId());
        recentActivity.setUser(user);
        recentActivityMapper.addRecentActivity(recentActivity);

    }

    public void backFraudCase(Long id) {
        fraudCaseMapper.backFraudCase(id);

        FraudCase fraudcase = fraudCaseMapper.getFraudCaseById(id);

        Long auditor_id = fraudCaseMapper.getAuditorId(id);

        User user = userMapper.getAuditorById(auditor_id);

        recentActivity = new RecentActivity();
        recentActivity.setMessage("Fraud Case type " + fraudcase.getCase_type() + " is moved to the drafting state.");
        user.setId(user.getId());
        recentActivity.setUser(user);
        recentActivityMapper.addRecentActivity(recentActivity);
    }

    public List<FraudCase> getPendingFraudCasesApprover() {
        return fraudCaseMapper.getPendingFraudCasesApprover();
    }

    public List<FraudCase> getApprovedFraudCasesApprover(Long user_id) {
        return fraudCaseMapper.getApprovedFraudCasesApprover(user_id);
    }

    public void approveFraudCase(FraudCase fraudCase) {
        fraudCaseMapper.approveFraudCase(fraudCase);

        User user = userMapper.getAuditorById(fraudCase.getApprover().getId());

        recentActivity = new RecentActivity();
        recentActivity.setMessage("Fraud Case type " + fraudCase.getCase_type() + " is approved.");

        user.setId(user.getId());
        recentActivity.setUser(user);
        recentActivityMapper.addRecentActivity(recentActivity);

    }

    public void cancelApprovedFraudCase(Long id) {
        Long approver_id = fraudCaseMapper.getApproverId(id);

        fraudCaseMapper.cancelApprovedFraudCase(id);
        FraudCase fraudcase = fraudCaseMapper.getFraudCaseById(id);

        User user = userMapper.getAuditorById(approver_id);

        recentActivity = new RecentActivity();
        recentActivity.setMessage("Fraud Case type " + fraudcase.getCase_type() + " is moved to the pending list.");

        user.setId(user.getId());
        recentActivity.setUser(user);
        recentActivityMapper.addRecentActivity(recentActivity);

    }

    public void deleteFraudCase(Long id) {

        fraudCaseMapper.deleteFraudCase(id);

        FraudCase fraudcase = fraudCaseMapper.getFraudCaseById(id);

        Long auditor_id = fraudCaseMapper.getAuditorId(id);

        User user = userMapper.getAuditorById(auditor_id);

        recentActivity = new RecentActivity();
        recentActivity.setMessage("Fraud Case type " + fraudcase.getCase_type() + " is deleted.");
        user.setId(user.getId());
        recentActivity.setUser(user);
        recentActivityMapper.addRecentActivity(recentActivity);

    }
}
