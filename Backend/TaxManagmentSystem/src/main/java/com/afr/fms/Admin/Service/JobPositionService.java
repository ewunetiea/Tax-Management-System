package com.afr.fms.Admin.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Entity.JobPosition;
import com.afr.fms.Admin.Entity.JobPositionRole;
import com.afr.fms.Admin.Entity.Role;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.JobPositionMapper;
import com.afr.fms.Admin.Mapper.UserMapper;

@Service
public class JobPositionService {

    @Autowired
    private JobPositionMapper jobPositionMapper;

    @Autowired
    private UserMapper userMapper;

    private static final Logger logger = LoggerFactory.getLogger(JobPositionService.class);

    public JobPositionRole getJobPositionsByRole(Role role) {
        return jobPositionMapper.getJobPositionsByRole(role);
    }

    public void manageJobPositions(JobPositionRole jobPositionRole) {
        Role role = jobPositionRole.getRole();
        List<JobPosition> assignedJobPositions = jobPositionMapper.getJobPositionsByRoleIdList(jobPositionRole.getRole().getId());
        List<JobPosition> newjobPositionList = (List<JobPosition>) jobPositionRole.getRole().getJobPositions();
        List<JobPosition> removedJobPositions = new ArrayList<>();
        List<Long> newJobPositionListIDs = newjobPositionList.stream().map(JobPosition::getId)
                .collect(Collectors.toList());
        for (JobPosition jobPosition : assignedJobPositions) {
            // if (!jobPositionList.contains(jobPosition)) {
            // jobPositionRest.add(jobPosition);
            // }
            if (!newJobPositionListIDs.contains(jobPosition.getId())) {
                removedJobPositions.add(jobPosition);
            }
        }

        jobPositionMapper.deleteJobPositionsByRole(jobPositionRole);

        // Get users by their role id

        List<User> userList = new ArrayList<>();
        // for (JobPosition jobPosition2 : newjobPositionList) {
        // userList.addAll(userMapper.getUsersByJobPositionId(jobPosition2.getId()));
        // }

        for (JobPosition jobPosition2 : newjobPositionList) {
            userList.addAll(
                    userMapper.getUsersByJobPositionIdandRolePosition(jobPosition2.getId(), role.getRole_position()));
        }

        try {

            // For every user, remove their roles and assign the new roles attached with
            // these job positions

            for (User user : userList) {
                userMapper.removeAllUserRoles(user.getId());
                userMapper.addUserRole(user.getId(), jobPositionRole.getRole());
                userMapper.changeUserStatus(user.getId(), true);

            }

            for (JobPosition jobPosition : removedJobPositions) {
                for (User user : userMapper.getUsersByJobPositionIdandRolePosition(jobPosition.getId(),
                        role.getRole_position())) {
                    userMapper.removeRolesByUserRole(user.getId(), role.getId());
                    if (user.getRoles() != null) {
                        if (user.getRoles().size() == 1) {
                            List<Role> userRoles = (List<Role>) user.getRoles();
                            if (userRoles.get(0).getId() == role.getId()) {
                                userMapper.changeUserStatus(user.getId(), false);
                            }
                        }
                    }

                }

            }

            // Finally update job position mapping with roles
            for (JobPosition jobPosition2 : newjobPositionList) {
                jobPositionMapper.addJobPositionsByRole(jobPositionRole.getRole(), jobPosition2);
            }

        } catch (Exception e) {
            logger.error("Error occurred during managing job positions for role ", e);
        }

    }

    public List<JobPosition> getJobPositions() {
        return jobPositionMapper.getJobPositions();
    }

    public List<JobPosition> getTotalJobPositions() {
        return jobPositionMapper.getTotalJobPositions();
    }

    public List<JobPosition> getMappedJobPositions() {
        return jobPositionMapper.getMappedJobPositions();
    }

    public List<JobPosition> getAllJobPositions() {
        return jobPositionMapper.getAllJobPositions();
    }

}
