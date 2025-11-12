package com.afr.fms.Approver.Service;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Approver.Entity.Announcement;
import com.afr.fms.Approver.Mapper.AnnouncementMapper;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;

@Service
public class AnnouncementService {
    @Autowired
    private AnnouncementMapper announcementMapper;

    @Autowired
    private RecentActivityMapper recentActivityMapper;

    User user = new User();

    RecentActivity recentActivity = new RecentActivity();

        @Transactional

    public List<Announcement> getOnGoingAnnouncements() {
        return announcementMapper.getOngoingAnnouncements();
    }


    @Transactional
      public Announcement getAnnouncementForDashBoard() {
        return announcementMapper.getAnnouncementForDashBoard();
    }


        @Transactional

    public List<Announcement> getArchivedAnnouncements() {
        return announcementMapper.getArchivedAnnouncements();
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
