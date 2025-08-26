package com.afr.fms.Common.Finding;

import com.afr.fms.Admin.Entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Finding {
private Long id;
private String content;
private String created_date;
private String modified_date;
private User user;
private String identifier;
    
}
