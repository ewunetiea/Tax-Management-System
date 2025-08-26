package com.afr.fms.Admin.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {
    private Long id;
    private Long user_id;
    private String feedback;
    private User user;
    private String response;
   
   
}
