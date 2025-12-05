package com.tms.Approver.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.tms.Admin.Entity.User;
import com.tms.Approver.Entity.Announcement;
import com.tms.Approver.Entity.AnnouncementFile;
import com.tms.Approver.Mapper.AnnouncementMapper;
import com.tms.Common.RecentActivity.RecentActivity;
import com.tms.Common.RecentActivity.RecentActivityMapper;
import com.tms.Maker.controller.TaxController;

@Service
public class AnnouncementService {
    @Autowired
    private AnnouncementMapper announcementMapper;

    @Autowired
    private RecentActivityMapper recentActivityMapper;

    private static final Logger logger = LoggerFactory.getLogger(TaxController.class);

    @Transactional

    public List<Announcement> getOnGoingAnnouncements(String role_type) {
        return announcementMapper.getOngoingAnnouncements(role_type);
    }

    @Transactional
    public Announcement getAnnouncementForDashBoard(String role_type) {
        return announcementMapper.getAnnouncementForDashBoard(role_type);

    }

    @Transactional

    public List<Announcement> getArchivedAnnouncements(String role_type) {

        return announcementMapper.getArchivedAnnouncements(role_type);

    }

    // @Transactional
    // public Long createCreateAnnouncement(Announcement announcement) {
    // Long announcement_id =
    // announcementMapper.createCreateAnnouncement(announcement);
    // recentActivity.setMessage(announcement.getTitle() + " is created ");
    // user.setId(announcement.getPosted_by());
    // recentActivity.setUser(user);
    // recentActivityMapper.addRecentActivity(recentActivity);
    // return announcement_id;
    // }

    @Transactional
    public Announcement createAnnouncement(Announcement announcement, MultipartFile[] files) throws IOException {
        String mainGuid = generateGuid();
        announcement.setMainGuid(mainGuid);
        // announcement.setReference_number(generateReferenceNumber());

        // Create upload directory once
        String uploadDir = Paths.get(System.getProperty("user.dir"), "announcementFiles").toString();
        File dir = new File(uploadDir);
        if (!dir.exists())
            dir.mkdirs();

        // Create announcement DB record
        Long announcementId = announcementMapper.createCreateAnnouncement(announcement);
        announcement.setId(announcementId);

        // Handle files
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                if (file.isEmpty())
                    continue;

                // Check file existence
                if (announcementMapper.checkFileNameExistance(file.getOriginalFilename())) {
                    announcement.setFileExsistance("Exists");
                    return announcement;
                }

                // Build file metadata (safe, no list indexing)
                AnnouncementFile af = new AnnouncementFile();
                af.setSupportId(mainGuid);
                af.setAnnouncement_id(announcementId);
                af.setFileName(file.getOriginalFilename());

                // Save metadata to DB FIRST
                announcementMapper.insertFile(af);

                // Save physical file
                File destination = new File(dir, file.getOriginalFilename());
                file.transferTo(destination);
            }
        }

        // Log recent activity ONCE
        RecentActivity ra = new RecentActivity();
        User u = new User();
        u.setId(announcement.getPosted_by());
        ra.setUser(u);
        ra.setMessage(announcement.getTitle() + " is created");
        recentActivityMapper.addRecentActivity(ra);
        announcement.setFileExsistance("notExist");
        return announcement;
    }

    // @Transactional
    // public void updateAnnouncement(Announcement announcement) {
    // announcementMapper.updateAnnouncements(announcement);
    // recentActivity.setMessage(announcement.getTitle() + " is updated ");
    // user.setId(announcement.getPosted_by());
    // recentActivity.setUser(user);
    // recentActivityMapper.addRecentActivity(recentActivity);
    // }

    @Transactional
    public void updateAnnouncement(Announcement announcement, MultipartFile[] files) throws IOException {
        try {
            String uploadDir = Paths.get(System.getProperty("user.dir"), "announcementFiles").toString();
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            announcementMapper.updateAnnouncements(announcement);

            if (announcement.getIsFileEdited()) {
                for (AnnouncementFile announcementFileToDelete : announcement.getPreviouseAnnouncementFile()) {

                    // Delete from DB
                    announcementMapper.deleteAnnouncementFile(announcementFileToDelete.getAnnouncement_id());

                    // Delete from folder
                    File existingFile = new File(dir, announcementFileToDelete.getFileName());
                    if (existingFile.exists()) {
                        if (existingFile.delete()) {
                        } else {

                        }
                    } else {

                    }
                }

                for (int i = 0; i < files.length; i++) {
                    MultipartFile file = files[i];
                    AnnouncementFile af = announcement.getAnnouncementFile().get(i);
                    af.setAnnouncement_id(announcement.getId());

                    if (!file.isEmpty()) {
                        String fileName = file.getOriginalFilename();
                        File destination = new File(dir, fileName);

                        file.transferTo(destination);
                        String fileId = generateGuid();
                        af.setSupportId(fileId);
                        af.setFileName(fileName);
                        announcementMapper.insertFile(af);
                    }
                }
            }
            // ✅ Log recent activity
             RecentActivity ra = new RecentActivity();
            User user = new User();
            user.setId(announcement.getPosted_by());
            ra.setUser(user);
            ra.setMessage(announcement.getTitle() + "  is updated ");
            recentActivityMapper.addRecentActivity(ra);
        } catch (Exception e) {
            logger.error("Error while updating announcement", e);
        }
    }

    public void deleteAnnouncement(Long id) {
        announcementMapper.deleteAnnouncement(id);
    }

    public Announcement getAnnouncementById(Long id) {
        return announcementMapper.getAnnouncementById(id);
    }

    public String generateGuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().toUpperCase();
    }

    // private String generateReferenceNumber() {
    //     Long lastRef = announcementMapper.getLastReferenceNumber();

    //     If DB returns null → start from REF1
    //     if (lastRef == null) {
    //         return "REF1";
    //     }

    //     long nextNumber = lastRef + 1;
    //     return "REF" + nextNumber;
    // }

}
