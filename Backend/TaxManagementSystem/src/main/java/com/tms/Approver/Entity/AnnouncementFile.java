package com.tms.Approver.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
    
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementFile {
    private String Id;
    private String fileName;
    private String extension;
    private String supportId;
    private Long announcement_id;
}

