package com.tms.Approver.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.tms.Admin.Entity.User;
import com.tms.Approver.Entity.Announcement;
import com.tms.Approver.Entity.AnnouncementFile;
import com.tms.Approver.Mapper.AnnouncementMapper;
import com.tms.Common.Exception.DuplicateFileException;
import com.tms.Common.FileManagement.FileStorageServiceImpl;
import com.tms.Common.RecentActivity.RecentActivity;
import com.tms.Common.RecentActivity.RecentActivityMapper;

@Service
@Transactional
public class AnnouncementService {

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Autowired
    private RecentActivityMapper recentActivityMapper;

    @Autowired
    private FileStorageServiceImpl fileStorageService;

    @Value("${file.storage.root}")
    private String rootPath;

    // ------------------ CREATE --------------------
    @Transactional
    public Announcement createAnnouncement(Announcement announcement, MultipartFile[] files) throws IOException {

        String mainGuid = generateGuid();
        announcement.setMainGuid(mainGuid);

        Long id = announcementMapper.createCreateAnnouncement(announcement);
        announcement.setId(id);

        if (files != null) {
            for (MultipartFile file : files) {
                if (file.isEmpty())
                    continue;

                String fileName = file.getOriginalFilename();

                // duplicate check → THROW exception to rollback!
                if (announcementMapper.checkFileNameExistance(fileName)) {
                    throw new DuplicateFileException("A file named '" + fileName + "' already exists");
                }

                // save file
                fileStorageService.saveFile(file, "announcementFile");

                AnnouncementFile af = new AnnouncementFile();
                af.setAnnouncement_id(id);
                af.setSupportId(mainGuid);
                af.setFileName(fileName);
                af.setExtension(getExtension(fileName));
                announcementMapper.insertFile(af);
            }
        }
        
        saveRecentActivity(announcement.getPosted_by(), "Announcement titled '" + announcement.getTitle() + "' is created ");
        announcement.setFileExsistance("notExist");

        return announcement;
    }

    // ------------------ UPDATE --------------------
    @Transactional
    public void updateAnnouncement(Announcement announcement, MultipartFile[] files) throws IOException {

        // ---- UPDATE MAIN RECORD ----
        announcementMapper.updateAnnouncements(announcement);

        if (announcement.getIsFileEdited() != null && announcement.getIsFileEdited()) {
            // ---------------- DELETE OLD FILES (DB + FOLDER) ----------------
            List<AnnouncementFile> oldFiles = announcement.getPreviouseAnnouncementFile();

            if (oldFiles != null) {
                for (AnnouncementFile old : oldFiles) {

                    // delete from DB
                    announcementMapper.deleteAnnouncementFile(old.getAnnouncement_id());

                    // delete from folder
                    boolean removed = fileStorageService.deleteFile(old.getFileName(), "announcementFile" );
                    if (!removed) {
                        throw new IOException("Failed to delete old file: " + old.getFileName());
                    }
                }
            }

            // ---------------- SAVE NEW FILES (DB + FOLDER) ----------------
            if (files != null) {
                for (MultipartFile file : files) {
                    if (file.isEmpty())
                        continue;

                    // save file first (folder)
                    String savedName = fileStorageService.saveFile(file, "announcementFile");
                    if (savedName == null) {
                        throw new IOException("Failed to save file: " + file.getOriginalFilename());
                    }

                    // then store file record in DB
                    AnnouncementFile af = new AnnouncementFile();
                    af.setAnnouncement_id(announcement.getId());
                    af.setSupportId(generateGuid());
                    af.setFileName(savedName);
                    af.setExtension(getExtension(savedName));

                    announcementMapper.insertFile(af);
                }
            }
        }
        saveRecentActivity(announcement.getPosted_by(), "Announcement titled '" + announcement.getTitle() + "' is updated ");
    }

    // ------------------ DELETE --------------------
   @Transactional
public void deleteAnnouncement(Announcement announcement) {

    // 1. Get files before deleting DB record
    List<AnnouncementFile> files = announcement.getAnnouncementFile();

    // 2. Delete Announcement row (DB CASCADE will delete file rows)
    announcementMapper.deleteAnnouncement(announcement.getId());

    // 3. Delete files from folder
    if (files != null && !files.isEmpty()) {
        for (AnnouncementFile oldFile : files) {
            try {
                // delete from DB
                announcementMapper.deleteAnnouncementFile(oldFile.getAnnouncement_id());
                
                // delete from folder
                boolean removed = fileStorageService.deleteFile(oldFile.getFileName(), "announcementFile");

                if (!removed) {
                    System.err.println("WARNING: Failed to delete file from folder: " + oldFile.getFileName());
                }

            } catch (Exception ex) {
                System.err.println("ERROR deleting file: " + oldFile.getFileName());
                ex.printStackTrace();
            }
        }
    }

    // 4. Save activity AFTER all operations
     saveRecentActivity(announcement.getPosted_by(), "Announcement titled '" + announcement.getTitle() + "' is deleted ");
}


    // ------------------ OTHER FUNCTIONS --------------------
    public String getExtension(String name) {
        return name.contains(".") ? name.substring(name.lastIndexOf(".")) : "";
    }

    public void saveRecentActivity(Long userId, String msg) {
        RecentActivity ra = new RecentActivity();
        User u = new User();
        u.setId(userId);
        ra.setUser(u);
        ra.setMessage(msg);
        recentActivityMapper.addRecentActivity(ra);
    }

    public Announcement getAnnouncementById(Long id) {
        return announcementMapper.getAnnouncementById(id);
    }

    public List<Announcement> getOnGoingAnnouncements(String role) {
        return announcementMapper.getOngoingAnnouncements(role);
    }

    public List<Announcement> getArchivedAnnouncements(String role) {
        return announcementMapper.getArchivedAnnouncements(role);
    }

    public Announcement getAnnouncementForDashBoard(String role) {
        return announcementMapper.getAnnouncementForDashBoard(role);
    }

    public String generateGuid() {
        return UUID.randomUUID().toString().toUpperCase();
    }
}
