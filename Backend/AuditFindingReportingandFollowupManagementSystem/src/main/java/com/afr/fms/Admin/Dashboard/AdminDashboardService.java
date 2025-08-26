package com.afr.fms.Admin.Dashboard;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.afr.fms.Admin.Entity.Region;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.BranchMapper;
import com.afr.fms.Admin.Mapper.RegionMapper;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Common.Permission.Mapper.FunctionalitiesMapper;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;

@Service
public class AdminDashboardService {
    @Autowired
    AdminDashboardMapper adminDashboardMapper;

    @Autowired
    private RecentActivityMapper recentActivityMapper;

    @Autowired
    private RegionMapper regionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BranchMapper branchMapper;

    @Autowired
    private FunctionalitiesMapper functionalitiesMapper;

    // List<Integer> roles_length_IS_MGT_INS = new ArrayList<>();

    List<String> roles_name_BFA = new ArrayList<>();

    public AdminDashboardData getDashBoardData(User user) {

        AdminDashboardData adminDashboardData = new AdminDashboardData();

        List<Integer> line_chart_data = new ArrayList<>();
        List<String> regions_name = new ArrayList<>();

        List<String> audits = new ArrayList<>();
        audits.add("IS");
        audits.add("MGT");
        audits.add("INS");
        audits.add("BFA");

        for (Region region : regionMapper.getRegions()) {
            regions_name.add(region.getName());
            for (String audit_type : audits) {
                line_chart_data.add(adminDashboardMapper.getUsersPerRegion(audit_type, region.getId()));
            }
        }

        List<Integer> permission_region_branch = new ArrayList<>();
        permission_region_branch.add(functionalitiesMapper.getAllFunctionalities().size());
        permission_region_branch.add(regionMapper.getRegions().size());
        permission_region_branch.add(branchMapper.getBranches().size());

        adminDashboardData.setDirectorates(regions_name);
        // adminDashboardData.setFinding_status(finding_status);
        adminDashboardData.setPolar_data(computePolarData(user.getId()));
        // adminDashboardData.setStacked_bar_chart_data(computeStackedBarChartData(user.getId()));
        adminDashboardData.setBar_chart_data(computeBarChartData());
        adminDashboardData.setHorizontal_bar_chart_data(computeHorizontalBarChartData());
        adminDashboardData.setRoles_name_BFA(roles_name_BFA);
        // adminDashboardData.setRoles_length_IS_MGT_INS(roles_length_IS_MGT_INS);

        // System.out.println(adminDashboardData.getRoles_length_IS_MGT_INS());

        adminDashboardData.setCard_data(computeCardData());
        // adminDashboardData.setDoughnut_data(computeDoughnutData(user.getId()));
        adminDashboardData.setAge_data(computeRadarAgeData());
        adminDashboardData.setLine_chart_data(line_chart_data);
        adminDashboardData.setRecentActivity(recentActivityMapper.getAllRecentActivity(user.getId()));
        return adminDashboardData;

    }

    public List<Integer> computePolarData(Long auditor_id) {
        List<User> users = userMapper.getUsers();
        int active_users = 0, inactive_users = 0, account_locked_users = 0, credential_expired_users = 0;
        List<Integer> polar_data = new ArrayList<>();

        for (User user2 : users) {
            if (user2.isStatus()) {
                active_users++;
            } else {
                inactive_users++;
            }
            if (user2.getUser_security() != null) {

                if (!user2.getUser_security().isAccountNonLocked()) {
                    account_locked_users++;
                }
                if (!user2.getUser_security().isCredentialsNonExpired()) {
                    credential_expired_users++;
                }
            }
        }
        polar_data.add(active_users);
        polar_data.add(inactive_users);
        polar_data.add(account_locked_users);
        polar_data.add(credential_expired_users);
        return polar_data;

    }

    // public List<Integer> computeDoughnutData(Long auditor_id) {
    // List<Integer> doughnut_data = new ArrayList<>();
    // doughnut_data.add(adminDashboardMapper.getTotalFollowupRejectedCounts(auditor_id));
    // doughnut_data.add(adminDashboardMapper.getTotalPartialyRectifiedCounts(auditor_id));
    // doughnut_data.add(adminDashboardMapper.getTotalApprovedCounts(auditor_id));
    // doughnut_data.add(adminDashboardMapper.getTotalRespondedCounts(auditor_id));
    // doughnut_data.add(adminDashboardMapper.getTotalRectifiedCounts(auditor_id));
    // return doughnut_data;
    // }

    public List<Integer> computeCardData() {
        List<Integer> card_data = new ArrayList<>();
        card_data.add(adminDashboardMapper.getSystemUsersByCategory("IS"));
        card_data.add(adminDashboardMapper.getSystemUsersByCategory("MGT"));
        card_data.add(adminDashboardMapper.getSystemUsersByCategory("BFA"));
        card_data.add(adminDashboardMapper.getSystemUsersByCategory("INS"));
        card_data.add(adminDashboardMapper.getUserLoginStatus(1));
        card_data.add(adminDashboardMapper.getUserLoginStatus(2));
        return card_data;
    }

    // public List<Integer> computeStackedBarChartData(Long auditor_id) {
    // List<Integer> stacked_bar_chart_data = new ArrayList<>();
    // for (int i = 1; i <= 12; i++) {
    // stacked_bar_chart_data.add(adminDashboardMapper.getTotalApprovedAuditsCountPerMonth(i,
    // auditor_id));
    // stacked_bar_chart_data
    // .add(adminDashboardMapper.getTotalApproverRejectedAuditsCountPerMonth(i,
    // auditor_id));
    // stacked_bar_chart_data.add(adminDashboardMapper.getTotalRespondedAuditsCountPerMonth(i,
    // auditor_id));
    // }
    // return stacked_bar_chart_data;
    // }

    // public List<List<Object>> computeLineChartData() {
    // List<List<Object>> region_users = new ArrayList<>();
    // List<Object> line_chart_data = new ArrayList<>();
    // List<Object> regions_name = new ArrayList<>();

    // List<String> audits = new ArrayList<>();
    // audits.add("IS");
    // audits.add("MGT");
    // audits.add("BFA");
    // audits.add("INS");

    // for (String audit_type : audits) {
    // for (Region region : regionMapper.getRegions()) {
    // regions_name.add(region.getName());
    // adminDashboardMapper.getUsersPerRegion(audit_type, region.getId());
    // }
    // }
    // region_users.add(regions_name);
    // region_users.add(line_chart_data);

    // return region_users;
    // }

    public List<Integer> computeBarChartData() {
        List<String> role_positions = new ArrayList<>();
        List<String> roles = new ArrayList<>();

        role_positions.add("IS");
        role_positions.add("MGT");
        role_positions.add("INS");

        roles.add("Approver");
        roles.add("Auditor");
        roles.add("Followup");
        roles.add("Reviewer");

        List<Integer> bar_chart_data_three_modules = new ArrayList<>();

        for (String rolePosition : role_positions) {
            for (String code : roles) {
                bar_chart_data_three_modules.add(adminDashboardMapper.getUsersPerRoleandAudit(rolePosition, code));
            }
        }

        return bar_chart_data_three_modules;
    }

    public List<Integer> computeRadarAgeData() {
        List<Integer> active_status = new ArrayList<>();
        active_status.add(adminDashboardMapper.getUserActiveStatusPerAudit(1, "IS"));
        active_status.add(adminDashboardMapper.getUserActiveStatusPerAudit(0, "IS"));
        active_status.add(adminDashboardMapper.getUserActiveStatusPerAudit(1, "MGT"));
        active_status.add(adminDashboardMapper.getUserActiveStatusPerAudit(0, "MGT"));
        active_status.add(adminDashboardMapper.getUserActiveStatusPerAudit(1, "BFA"));
        active_status.add(adminDashboardMapper.getUserActiveStatusPerAudit(0, "BFA"));
        active_status.add(adminDashboardMapper.getUserActiveStatusPerAudit(1, "INS"));
        active_status.add(adminDashboardMapper.getUserActiveStatusPerAudit(0, "INS"));
        return active_status;
    }

    public List<Integer> computeHorizontalBarChartData() {
        List<String> roles = new ArrayList<>();
        roles.add("Auditor");
        roles.add("Reviewer");
        roles.add("COMPILER");
        roles.add("Approver");
        roles.add("BRANCHM");
        roles.add("REGIONALD");

        List<Integer> bar_chart_data_bfa = new ArrayList<>();

        for (String code : roles) {
            bar_chart_data_bfa.add(adminDashboardMapper.getUsersPerRoleandAudit("BFA", code));
        }
        return bar_chart_data_bfa;
    }

    // public List<Integer> computeHorizontalBarChartData(Long auditor_id) {
    // List<Integer> total_data = new ArrayList<>();
    // for (int i = 1; i <= 12; i++) {
    // total_data.add(adminDashboardMapper.getTotalDraftedAuditsCountPerMonth(i,
    // auditor_id));
    // total_data.add(adminDashboardMapper.getTotalPassedAuditsCountPerMonth(i,
    // auditor_id));
    // total_data.add(adminDashboardMapper.getTotalReviewerRejectedAuditsCountPerMonth(i,
    // auditor_id));
    // total_data.add(adminDashboardMapper.getTotalReviewedAuditsCountPerMonth(i,
    // auditor_id));
    // }
    // return total_data;
    // }

}
