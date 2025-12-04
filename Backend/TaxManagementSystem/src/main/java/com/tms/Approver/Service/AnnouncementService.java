package com.tms.Approver.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tms.Admin.Entity.User;
import com.tms.Approver.Entity.Announcement;
import com.tms.Approver.Entity.AnnouncementFile;
import com.tms.Approver.Entity.AnnouncementPayload;
import com.tms.Approver.Mapper.AnnouncementMapper;
import com.tms.Common.RecentActivity.RecentActivity;
import com.tms.Common.RecentActivity.RecentActivityMapper;
import com.tms.Maker.entity.Tax;
import com.tms.Maker.entity.TaxFile;

@Service
public class AnnouncementService {
    @Autowired
    private AnnouncementMapper announcementMapper;

    @Autowired
    private RecentActivityMapper recentActivityMapper;

    User user = new User();

    RecentActivity recentActivity = new RecentActivity();

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
    //     Long announcement_id = announcementMapper.createCreateAnnouncement(announcement);
    //     recentActivity.setMessage(announcement.getTitle() + "  is created ");
    //     user.setId(announcement.getPosted_by());
    //     recentActivity.setUser(user);
    //     recentActivityMapper.addRecentActivity(recentActivity);
    //     return announcement_id;

    // }

    @Transactional
    public Announcement createCreateAnnouncement(Announcement announcement, MultipartFile[] files) throws IOException {
        String mainGuid = generateGuid();

        if (files != null && files.length > 0 && announcement.getAnnouncementFile() != null) {
            String uploadDir = Paths.get(System.getProperty("user.dir"), "announcementFiles").toString();

            // String uploadDir = "\\\\10.10.101.76\\fileUploadFolder"; // Use IP upload
            // from other server

            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Check for existing files in the database
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    // Check if the file already exists in the database
                    if (announcementMapper.checkFilnameExistance(file.getOriginalFilename())) {
                        announcement.setFileExsistance("Exists");
                        // Notify user that the file already exists
                        return announcement;
                    }
                }
            }

            announcement.setMainGuid(mainGuid);
            announcement.setReference_number(generateReferenceNumber());

            // Create tax entry
            Long announcement_id = announcementMapper.createCreateAnnouncement(announcement);
            announcement.setId(announcement_id);
            announcement.setFileExsistance("notExist");

            // Process and store the files
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                AnnouncementFile af = announcement.getAnnouncementFile().get(i);
                af.setAnnouncement_id(announcement_id);

                if (!file.isEmpty()) {
                    File destination = new File(dir, file.getOriginalFilename());

                    // Transfer the file to the destination
                    file.transferTo(destination);

                    // Generate ID and update the taxFile object
                    String fileId = generateGuid();
                    af.setId(mainGuid);
                    af.setSupportId(fileId);
                    af.setFileName(file.getOriginalFilename());

                    // Insert the file record in the database
                    announcementMapper.insertFile(af);

                    User user = new User();
                    recentActivity.setMessage(announcement.getTitle() + "  is created ");
                    user.setId(announcement.getPosted_by());
                    recentActivity.setUser(user);
                    recentActivityMapper.addRecentActivity(recentActivity);

                }
            }
        }
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
                    announcementMapper.deleteAnnouncementFile(announcementFileToDelete.getFileName());

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
            User user = new User();
            user.setId(announcement.getPosted_by());
            recentActivity.setUser(user);
            recentActivity.setMessage(announcement.getTitle() + "  is updated ");
            recentActivityMapper.addRecentActivity(recentActivity);
        } catch (Exception e) {
            // TODO: handle exception
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

     private String generateReferenceNumber() {
        String lastRef = announcementMapper.getLastReferenceNumber();
        if (lastRef == null || lastRef.isEmpty()) {
            return "REF1";
        }

        // Extract numeric part — e.g., TAX12 → 12
        String numberPart = lastRef.replaceAll("[^0-9]", "");
        int nextNumber = 1;
        try {
            nextNumber = Integer.parseInt(numberPart) + 1;
        } catch (NumberFormatException e) {
            // fallback if number part is invalid
            nextNumber = 1;
        }

        return "REF" + nextNumber;
    }

}
