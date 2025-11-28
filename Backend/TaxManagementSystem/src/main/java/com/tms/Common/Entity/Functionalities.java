package com.tms.Common.Entity;

import com.tms.Admin.Entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Functionalities {
    private Long id;
    private String name;
    private String description;
    private boolean status;
    private String category;
    private String method;
    private User user;
}