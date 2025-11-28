package com.tms.Approver.Service;

import java.util.List;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tms.Admin.Entity.User;
import com.tms.Approver.Entity.Announcement;
import com.tms.Approver.Entity.AnnouncementPayload;
import com.tms.Approver.Mapper.AnnouncementMapper;
import com.tms.Common.RecentActivity.RecentActivity;
import com.tms.Common.RecentActivity.RecentActivityMapper;

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

        return announcementMapper.getArchivedAnnouncements( role_type);

    }

    @Transactional
    public Long createCreateAnnouncement(Announcement announcement) {
        Long announcement_id = announcementMapper.createCreateAnnouncement(announcement);

        recentActivity.setMessage(announcement.getTitle() + "  is created ");
        user.setId(announcement.getPosted_by());
        recentActivity.setUser(user);
        recentActivityMapper.addRecentActivity(recentActivity);

        return announcement_id;

    }

    @Transactional
    public void updateAnnouncement(Announcement announcement) {
        announcementMapper.updateAnnouncements(announcement);
        recentActivity.setMessage(announcement.getTitle() + "   is updated ");
        user.setId(announcement.getPosted_by());
        recentActivity.setUser(user);
        recentActivityMapper.addRecentActivity(recentActivity);
    }

    public void deleteAnnouncement(Long id) {
        announcementMapper.deleteAnnouncement(id);
    }

    public Announcement getAnnouncementById(Long id) {
        return announcementMapper.getAnnouncementById(id);
    }
}
