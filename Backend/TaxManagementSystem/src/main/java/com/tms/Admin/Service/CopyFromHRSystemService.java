package com.tms.Admin.Service;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.Admin.Entity.Branch;
import com.tms.Admin.Entity.JobPosition;
import com.tms.Admin.Entity.Log;
import com.tms.Admin.Entity.Region;
import com.tms.Admin.Entity.Role;
import com.tms.Admin.Entity.User;
import com.tms.Admin.Entity.UserCopyFromHR;
import com.tms.Admin.Mapper.BranchMapper;
import com.tms.Admin.Mapper.CopyHRUsersMapper;
import com.tms.Admin.Mapper.JobPositionMapper;
import com.tms.Admin.Mapper.LogMapper;
import com.tms.Admin.Mapper.RegionMapper;
import com.tms.Admin.Mapper.RoleMapper;
import com.tms.Admin.Mapper.UserMapper;
import com.tms.Admin.Mapper.UserRoleMapper;

import jakarta.transaction.Transactional;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

// import org.apache.hc.client5.http.config.RequestConfig;
// import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
// import org.apache.hc.client5.http.impl.classic.HttpClients;
// import org.apache.hc.core5.util.Timeout;

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
        System.out.println("HR Scheduler triggered after 50 seconds of app start");
        Date date = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = formatter.format(date);
        final String uri = "https://hr.awashbank.com/hr/api/empInfo/" + formattedDate;

        // final String uri = "https://hr.awashbank.com/hr/api/empInfo/2020-01-01 00:00:00";

        System.out.println("hr system running...");

        RestTemplate restTemplate = new RestTemplate();
        UserCopyFromHR[] users_copy = restTemplate.getForObject(uri, UserCopyFromHR[].class);

        System.out.println("hr system copyin...");

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
        System.out.println("hr system copying done...");
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







    // // @Scheduled(cron = "0 24 10 * * ?")
    //  @Scheduled(initialDelay = 50000, fixedDelay = Long.MAX_VALUE) // 50000 ms = 50 seconds

    //   @Transactional
    // public void monthlyScheduledCopyUsersFromHrSystem() {


    //     System.out.println("________scheduler____________");
    //     // if (scheduleService.checkScheduleStatus("copy_users_info_hr_system")) {
    //     System.out.println("HR Scheduler triggered after 50 seconds of app start");
    //     Date date = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
    //     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //     String formattedDate = formatter.format(date);
    //     // final String uri = "https://hr.awashbank.com/hr/api/empInfo/" + formattedDate;

    //      final String uri = "https://hr.awashbank.com/hr/api/empInfo/2020-01-01 00:00:00";

    //     System.out.println("hr system running...");

    //     RestTemplate restTemplate = new RestTemplate();
    //     UserCopyFromHR[] users_copy = restTemplate.getForObject(uri, UserCopyFromHR[].class);

    //     System.out.println("hr system copyin...");

    //     List<String> jobTitles = jobPositionMapper.getAllJobTitles();
    //     List<String> newJobTitles = new ArrayList<>();

    //     List<String> empIds = userCopyFromHRMapper.allUsersEmployeeIDs();
    //     List<String> newEmpIds = new ArrayList<>();

    //     List<String> regionNames = regionMapper.getRegionNames();
    //     List<String> newRegionNames = new ArrayList<>();

    //     List<String> branchNames = branchMapper.getBranchName();
    //     List<String> newBranchNames = new ArrayList<>();

    //     JobPosition job_position = new JobPosition();

    //     for (UserCopyFromHR userCopyFromHR : users_copy) {
    //         try {
    //             if (!userCopyFromHR.getStatus()) {
    //                 Region region = new Region();
    //                 Branch branch = new Branch();
    //                 User user = new User();
    //                 User user_store = new User();

    //                 if (!regionNames.contains(userCopyFromHR.getDeptLocation()) && !newRegionNames.contains(userCopyFromHR.getDeptLocation()) && userCopyFromHR.getDeptLocation() != null) {
    //                     newRegionNames.add(userCopyFromHR.getDeptLocation());
    //                     region.setName(userCopyFromHR.getDeptLocation());
    //                     region.setCode(processCodeGeneration(region.getName(), true));
    //                     regionMapper.createRegion(region);
    //                 }

    //                 if (!branchNames.contains(userCopyFromHR.getUnit()) && !newBranchNames.contains(userCopyFromHR.getUnit()) && userCopyFromHR.getUnit() != null) {
    //                     newBranchNames.add(userCopyFromHR.getUnit());
    //                     branch.setName(userCopyFromHR.getUnit());
    //                     branch.setCode(processCodeGeneration(branch.getName(), false));
    //                     branch.setRegion(regionMapper.getRegionByName(userCopyFromHR.getDeptLocation()));
    //                     branchMapper.createBranch(branch);
    //                 }

    //                 if (!empIds.contains(userCopyFromHR.getEmpId()) && !newEmpIds.contains(userCopyFromHR.getEmpId().trim())) {
    //                     newEmpIds.add(userCopyFromHR.getEmpId().trim());
    //                     userCopyFromHRMapper.addUserCopyHR(userCopyFromHR);
    //                 } else {
    //                     userCopyFromHRMapper.deleteByEmployeeId(userCopyFromHR.getEmpId().trim());
    //                     userCopyFromHRMapper.addUserCopyHR(userCopyFromHR);
    //                 }

    //                 if (!jobTitles.contains(userCopyFromHR.getPosition()) && !newJobTitles.contains(userCopyFromHR.getPosition())) {
    //                     newJobTitles.add(userCopyFromHR.getPosition());
    //                     job_position.setTitle(userCopyFromHR.getPosition());
    //                     jobPositionMapper.addJobPosition(job_position);
    //                 }

    //                 user = userMapper.findByEmployeeIDScheduler(userCopyFromHR.getEmpId());
    //                 user_store = user;

    //                 if (user != null) {
    //                     if (!userCopyFromHR.getUnit().toLowerCase().contains("region")) {
    //                         if (user.getBranch() != null) {
    //                             if (!user.getBranch().getName().trim().equalsIgnoreCase(userCopyFromHR.getUnit().trim())) {
    //                                 user_store.setBranch(branchMapper.getBranchByName(userCopyFromHR.getUnit()));
    //                             }
    //                         } else {
    //                             user_store.setBranch(branchMapper.getBranchByName(userCopyFromHR.getUnit()));
    //                         }
    //                     }

    //                     else {
    //                         if (userCopyFromHR.getUnit().contains("Region")) {
    //                             if (user.getRegion() != null) {
    //                                 if (!user.getRegion().getName().trim().equalsIgnoreCase(userCopyFromHR.getDeptLocation().trim())) {
    //                                     user_store.setRegion(regionMapper.getRegionByName(userCopyFromHR.getDeptLocation()));
    //                                 }
    //                             }
    //                         } else {
    //                             user_store.setRegion(regionMapper.getRegionByName(userCopyFromHR.getDeptLocation()));
    //                         }
    //                     }

    //                     // check the following code carefully
    //                     if (user.getJobPosition() != null)
    //                         if (!user.getJobPosition().getTitle().trim().equalsIgnoreCase(userCopyFromHR.getPosition().trim())) {
    //                             List<JobPosition> registeredJobPositions = jobPositionMapper.getJobPositionsByTitle(userCopyFromHR.getPosition());
    //                             JobPosition jobPositionForUser = registeredJobPositions.get(0);
    //                             List<Role> roles = new ArrayList<>();
    //                             for (JobPosition jobPosition : registeredJobPositions) {
    //                                 roles = jobPositionMapper.getRoleByJobPositionId(jobPosition.getId());
    //                                 if (roles != null) {
    //                                     jobPositionForUser = jobPosition;
    //                                     break;
    //                                 }
    //                             }

    //                             user_store.setJobPosition(jobPositionForUser);

                                
    //                             if (roles != null) {
    //                                 if (!roles.containsAll(user.getRoles())) {
    //                                     user_store.setStatus(true);
    //                                     userRoleMapper.removeAllUserRoles(user.getId());
    //                                     for (Role role : roles) {
    //                                         userRoleMapper.addUserRole(user.getId(), role);
    //                                     }



    //                                 }
    //                             } else {
    //                                 userRoleMapper.removeAllUserRoles(user.getId());
    //                                 user_store.setStatus(false);
    //                             }

    //                         }
    //                     userMapper.updateUserScheduler(user_store);
    //                 }

    //             }
    //         } catch (Exception e) {
    //             Log log = new Log();
    //             log.setName("Schedule that copy Users From HR System");
    //             log.setException(e.getMessage());
    //             logMapper.addLog(log);
    //             logger.error("Error occurred while processing HR Data", e);

    //         }
    //     }
    //     System.out.println("hr system copying done...");
    //     // }

    // }


    // @Scheduled(initialDelay = 50000, fixedDelay = Long.MAX_VALUE)
public void monthlyScheduledCopyUsersFromHrSystem() {

    logger.info("________scheduler____________");
    logger.info("HR Scheduler triggered after 50 seconds of app start");

    final String uri = "https://hr.awashbank.com/hr/api/empInfo/2020-01-01 00:00:00";

    try {
        fetchAllUsersFromHrStreaming(uri);
    } catch (Exception e) {
        logger.error("HR Scheduler failed, will retry next run", e);
    }

    logger.info("HR system copying done...");
}

private void fetchAllUsersFromHrStreaming(String uri) {

    HttpComponentsClientHttpRequestFactory factory =
            new HttpComponentsClientHttpRequestFactory();
    factory.setConnectTimeout(30_000);
    // factory.setReadTimeout(900_000); // 15 minutes

    RestTemplate restTemplate = new RestTemplate(factory);

    restTemplate.execute(uri, HttpMethod.GET, null, response -> {

        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();

        try (InputStream is = response.getBody();
             JsonParser parser = jsonFactory.createParser(is)) {

            if (parser.nextToken() != JsonToken.START_ARRAY) {
                throw new RuntimeException("HR API did not return JSON array");
            }

            int counter = 0;

            while (parser.nextToken() == JsonToken.START_OBJECT) {
                try {
                    UserCopyFromHR user =
                            mapper.readValue(parser, UserCopyFromHR.class);

                    processSingleUser(user);
                    counter++;

                    if (counter % 1000 == 0) {
                        logger.info("Processed {} HR users", counter);
                    }

                } catch (Exception ex) {
                    logger.error("Failed to process one HR record, skipped", ex);
                }
            }

            logger.info("Total HR users processed: {}", counter);
        }

        return null;
    });
}

@Transactional
public void processSingleUser(UserCopyFromHR userCopyFromHR) {

    if (userCopyFromHR == null) return;
    if (Boolean.TRUE.equals(userCopyFromHR.getStatus())) return;
    if (userCopyFromHR.getEmpId() == null) return;

    try {
        List<String> jobTitles = jobPositionMapper.getAllJobTitles();
        List<String> empIds = userCopyFromHRMapper.allUsersEmployeeIDs();
        List<String> regionNames = regionMapper.getRegionNames();
        List<String> branchNames = branchMapper.getBranchName();

        Region region;
        Branch branch;
        JobPosition jobPosition = new JobPosition();

        /* -------- REGION -------- */
        if (userCopyFromHR.getDeptLocation() != null
                && !regionNames.contains(userCopyFromHR.getDeptLocation())) {

            region = new Region();
            region.setName(userCopyFromHR.getDeptLocation());
            region.setCode(processCodeGeneration(region.getName(), true));
            regionMapper.createRegion(region);
        }

        /* -------- BRANCH -------- */
        if (userCopyFromHR.getUnit() != null
                && !branchNames.contains(userCopyFromHR.getUnit())) {

            branch = new Branch();
            branch.setName(userCopyFromHR.getUnit());
            branch.setCode(processCodeGeneration(branch.getName(), false));
            branch.setRegion(regionMapper.getRegionByName(userCopyFromHR.getDeptLocation()));
            branchMapper.createBranch(branch);
        }

        /* -------- USER COPY HR -------- */
        if (!empIds.contains(userCopyFromHR.getEmpId().trim())) {
            userCopyFromHRMapper.addUserCopyHR(userCopyFromHR);
        } else {
            userCopyFromHRMapper.deleteByEmployeeId(userCopyFromHR.getEmpId().trim());
            userCopyFromHRMapper.addUserCopyHR(userCopyFromHR);
        }

        /* -------- JOB POSITION -------- */
        if (userCopyFromHR.getPosition() != null
                && !jobTitles.contains(userCopyFromHR.getPosition())) {

            jobPosition.setTitle(userCopyFromHR.getPosition());
            jobPositionMapper.addJobPosition(jobPosition);
        }

        /* -------- USER UPDATE -------- */
        User user = userMapper.findByEmployeeIDScheduler(userCopyFromHR.getEmpId());
        if (user == null) return;

        if (!userCopyFromHR.getUnit().toLowerCase().contains("region")) {
            user.setBranch(branchMapper.getBranchByName(userCopyFromHR.getUnit()));
        } else {
            user.setRegion(regionMapper.getRegionByName(userCopyFromHR.getDeptLocation()));
        }

        if (user.getJobPosition() != null &&
                !user.getJobPosition().getTitle().equalsIgnoreCase(userCopyFromHR.getPosition())) {

            List<JobPosition> jobPositions =
                    jobPositionMapper.getJobPositionsByTitle(userCopyFromHR.getPosition());

            JobPosition jp = jobPositions.get(0);
            List<Role> roles = jobPositionMapper.getRoleByJobPositionId(jp.getId());

            user.setJobPosition(jp);

            userRoleMapper.removeAllUserRoles(user.getId());
            if (roles != null) {
                for (Role role : roles) {
                    userRoleMapper.addUserRole(user.getId(), role);
                }
                user.setStatus(true);
            } else {
                user.setStatus(false);
            }
        }

        userMapper.updateUserScheduler(user);

    } catch (Exception e) {
        Log log = new Log();
        log.setName("Schedule that copy Users From HR System");
        log.setException(e.getMessage());
        logMapper.addLog(log);
        logger.error("Error occurred while processing HR user", e);
    }
}




}
