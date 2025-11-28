package com.tms.Approver.Entity;

import java.sql.Date;
import com.tms.Admin.Entity.User;
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

}
