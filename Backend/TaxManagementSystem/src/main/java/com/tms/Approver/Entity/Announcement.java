package com.tms.Approver.Entity;

import java.sql.Date;
import java.util.List;
import com.tms.Admin.Entity.User;
import com.tms.Maker.entity.TaxFile;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Announcement {
    private Long id;
    private String title;
    private String message;
    private Long posted_by;
    private User user;
    private String audience;
    private Date created_date;
    private Date expiry_date;
    private byte[] image;
    private String postedBy;
    private Boolean isFileEdited;
    private List<AnnouncementFile> previouseAnnouncementFile;
    private List<AnnouncementFile> announcementFile;
    private String fileExsistance;
    private String mainGuid;
}
