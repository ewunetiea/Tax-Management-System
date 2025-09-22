package com.afr.fms.Admin.Dashboard;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor

@NoArgsConstructor
public class AgeRadarDTO {
    private String category;
    private int active;
    private int inactive;

    // getters and setters
}