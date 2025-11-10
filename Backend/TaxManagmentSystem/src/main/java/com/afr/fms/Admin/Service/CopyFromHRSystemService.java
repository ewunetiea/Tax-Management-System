package com.afr.fms.Admin.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.afr.fms.Admin.Entity.Branch;
import com.afr.fms.Admin.Entity.JobPosition;
import com.afr.fms.Admin.Entity.Log;
import com.afr.fms.Admin.Entity.Region;
import com.afr.fms.Admin.Entity.Role;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Entity.UserCopyFromHR;
import com.afr.fms.Admin.Mapper.BranchMapper;
import com.afr.fms.Admin.Mapper.CopyHRUsersMapper;
import com.afr.fms.Admin.Mapper.JobPositionMapper;
import com.afr.fms.Admin.Mapper.LogMapper;
import com.afr.fms.Admin.Mapper.RegionMapper;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Admin.Mapper.UserRoleMapper;
import com.afr.fms.Admin.Mapper.RoleMapper;

@Service
public class CopyFromHRSystemService {

    @Autowired
    CopyHRUsersMapper userCopyFromHRMapper;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRoleMapper userRoleMapper;

    @Autowired
    BranchMapper branchMapper;

    @Autowired
    RegionMapper regionMapper;

    @Autowired
    JobPositionMapper jobPositionMapper;

    @Autowired
    LogMapper logMapper;

    @Autowired
    ScheduleService scheduleService;

    private static final Logger logger = LoggerFactory.getLogger(CopyFromHRSystemService.class);

    // @Scheduled(cron = "0 0 19 * * ?")
    // @Scheduled(initialDelay = 50000, fixedDelay = Long.MAX_VALUE) // 50000 ms = 50 seconds

    @Transactional
    public void scheduledCopyUsersFromHrSystem() {
        // if (scheduleService.checkScheduleStatus("copy_users_info_hr_system")) {
        Date date = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = formatter.format(date);
        final String uri = "https://hr.awashbank.com/hr/api/empInfo/" + formattedDate;

        // final String uri = "https://hr.awashbank.com/hr/api/empInfo/2020-01-01 00:00:00";

        RestTemplate restTemplate = new RestTemplate();
        UserCopyFromHR[] users_copy = restTemplate.getForObject(uri, UserCopyFromHR[].class);

        List<String> jobTitles = jobPositionMapper.getAllJobTitles();
        List<String> newJobTitles = new ArrayList<>();

        List<String> empIds = userCopyFromHRMapper.allUsersEmployeeIDs();
        List<String> newEmpIds = new ArrayList<>();

        List<String> regionNames = regionMapper.getRegionNames();
        List<String> newRegionNames = new ArrayList<>();

        List<String> branchNames = branchMapper.getBranchName();
        List<String> newBranchNames = new ArrayList<>();

        JobPosition job_position = new JobPosition();

        for (UserCopyFromHR userCopyFromHR : users_copy) {
            try {
                if (!userCopyFromHR.getStatus()) {
                    Region region = new Region();
                    Branch branch = new Branch();
                    User user = new User();
                    User user_store = new User();

                    if (!regionNames.contains(userCopyFromHR.getDeptLocation()) && !newRegionNames.contains(userCopyFromHR.getDeptLocation()) && userCopyFromHR.getDeptLocation() != null) {
                        newRegionNames.add(userCopyFromHR.getDeptLocation());
                        region.setName(userCopyFromHR.getDeptLocation());
                        region.setCode(processCodeGeneration(region.getName(), true));
                        regionMapper.createRegion(region);
                    }

                    if (!branchNames.contains(userCopyFromHR.getUnit()) && !newBranchNames.contains(userCopyFromHR.getUnit()) && userCopyFromHR.getUnit() != null) {
                        newBranchNames.add(userCopyFromHR.getUnit());
                        branch.setName(userCopyFromHR.getUnit());
                        branch.setCode(processCodeGeneration(branch.getName(), false));
                        branch.setRegion(regionMapper.getRegionByName(userCopyFromHR.getDeptLocation()));
                        branchMapper.createBranch(branch);
                    }

                    if (!empIds.contains(userCopyFromHR.getEmpId()) && !newEmpIds.contains(userCopyFromHR.getEmpId().trim())) {
                        newEmpIds.add(userCopyFromHR.getEmpId().trim());
                        userCopyFromHRMapper.addUserCopyHR(userCopyFromHR);
                    } else {
                        userCopyFromHRMapper.deleteByEmployeeId(userCopyFromHR.getEmpId().trim());
                        userCopyFromHRMapper.addUserCopyHR(userCopyFromHR);
                    }

                    if (!jobTitles.contains(userCopyFromHR.getPosition()) && !newJobTitles.contains(userCopyFromHR.getPosition())) {
                        newJobTitles.add(userCopyFromHR.getPosition());
                        job_position.setTitle(userCopyFromHR.getPosition());
                        jobPositionMapper.addJobPosition(job_position);
                    }

                    user = userMapper.findByEmployeeIDScheduler(userCopyFromHR.getEmpId());
                    user_store = user;

                    if (user != null) {
                        if (!userCopyFromHR.getUnit().toLowerCase().contains("region")) {
                            if (user.getBranch() != null) {
                                if (!user.getBranch().getName().trim().equalsIgnoreCase(userCopyFromHR.getUnit().trim())) {
                                    user_store.setBranch(branchMapper.getBranchByName(userCopyFromHR.getUnit()));
                                }
                            } else {
                                user_store.setBranch(branchMapper.getBranchByName(userCopyFromHR.getUnit()));
                            }
                        }

                        else {
                            if (userCopyFromHR.getUnit().contains("Region")) {
                                if (user.getRegion() != null) {
                                    if (!user.getRegion().getName().trim().equalsIgnoreCase(userCopyFromHR.getDeptLocation().trim())) {
                                        user_store.setRegion(regionMapper.getRegionByName(userCopyFromHR.getDeptLocation()));
                                    }
                                }
                            } else {
                                user_store.setRegion(regionMapper.getRegionByName(userCopyFromHR.getDeptLocation()));
                            }
                        }

                        // check the following code carefully
                        if (user.getJobPosition() != null)
                            if (!user.getJobPosition().getTitle().trim().equalsIgnoreCase(userCopyFromHR.getPosition().trim())) {
                                List<JobPosition> registeredJobPositions = jobPositionMapper.getJobPositionsByTitle(userCopyFromHR.getPosition());
                                JobPosition jobPositionForUser = registeredJobPositions.get(0);
                                List<Role> roles = new ArrayList<>();
                                for (JobPosition jobPosition : registeredJobPositions) {
                                    roles = jobPositionMapper.getRoleByJobPositionId(jobPosition.getId());
                                    if (roles != null) {
                                        jobPositionForUser = jobPosition;
                                        break;
                                    }
                                }

                                user_store.setJobPosition(jobPositionForUser);
                                if (roles != null) {
                                    if (!roles.containsAll(user.getRoles())) {
                                        user_store.setStatus(true);
                                        userRoleMapper.removeAllUserRoles(user.getId());
                                        for (Role role : roles) {
                                            userRoleMapper.addUserRole(user.getId(), role);
                                        }
                                    }
                                } else {
                                    userRoleMapper.removeAllUserRoles(user.getId());
                                    user_store.setStatus(false);
                                }

                            }
                        userMapper.updateUserScheduler(user_store);
                    }

                }
            } catch (Exception e) {
                Log log = new Log();
                log.setName("Schedule that copy Users From HR System");
                log.setException(e.getMessage());
                logMapper.addLog(log);
                logger.error("Error occurred while processing HR Data", e);

            }
        }
        // }

    }

    public String processCodeGeneration(String name, boolean isRegion) {
        char[] generatedCode = generateCode(10, name);
        String code = "";
        for (char c : generatedCode) {
            code = code + c;
        }
        if (isRegion) {
            List<Region> regions = regionMapper.getRegions();
            while (!isCodeNotExist(code, regions)) {
                generatedCode = generateCode(10, name);
                code = "";
                for (char c : generatedCode) {
                    code = code + c;
                }
            }
        } else {
            List<Branch> branches = branchMapper.getBranches();
            while (!isCodeNotExistBranch(code, branches)) {
                generatedCode = generateCode(10, name);
                code = "";
                for (char c : generatedCode) {
                    code = code + c;
                }
            }
        }
        return code;
    }

    public boolean isCodeNotExist(String code, List<Region> regions) {
        for (Region region : regions) {
            if (region.getCode().equalsIgnoreCase(code)) {
                return false;
            }
        }
        return true;
    }

    public boolean isCodeNotExistBranch(String code, List<Branch> branches) {
        for (Branch branch : branches) {
            if (branch.getCode().equalsIgnoreCase(code)) {
                return false;
            }
        }
        return true;
    }

    public char[] generateCode(int length, String code) {
        String[] spilitedCode = StringUtils.split(code);
        String storeChar = "";
        Random random = new Random();
        char[] generatedCode = new char[length];
        int index = 0;
        for (String c : spilitedCode) {
            generatedCode[index++] = c.charAt(0);
            storeChar = storeChar + c;
        }
        String combinedChars = storeChar.toUpperCase() + storeChar.toLowerCase();
        for (int i = index; i < length; i++) {
            generatedCode[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return generatedCode;
    }

    // @Scheduled(cron = "0 24 10 * * ?")
    @Async
    public void replacingHRData() {
        // if (scheduleService.checkScheduleStatus("replace_hr_data")) {
        try {
            // Date date = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
            // SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // String formattedDate = formatter.format(date);
            // final String uri = "https://hr.awashbank.com/hr/api/empInfo/" +
            // formattedDate;

            final String uri = "https://hr.awashbank.com/hr/api/empInfo/1980-01-01 00:00:00";

            RestTemplate restTemplate = new RestTemplate();
            UserCopyFromHR[] users_copy = restTemplate.getForObject(uri, UserCopyFromHR[].class);

            for (UserCopyFromHR userCopyFromHR : users_copy) {
                if (!userCopyFromHR.getStatus()) {
                    userCopyFromHRMapper.deleteByEmployeeId(userCopyFromHR.getEmpId());
                    userCopyFromHRMapper.addUserCopyHR(userCopyFromHR);
                }
            }

        } catch (Exception e) {
            logger.error("Error occurred while replacing HR Data", e);
        }
        // }

    }

}
