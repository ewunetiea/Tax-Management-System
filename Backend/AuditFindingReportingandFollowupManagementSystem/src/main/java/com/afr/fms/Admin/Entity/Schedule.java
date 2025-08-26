package com.afr.fms.Admin.Entity;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
     private Long id;
    private String name;
    private String description;
    private boolean status;
}
