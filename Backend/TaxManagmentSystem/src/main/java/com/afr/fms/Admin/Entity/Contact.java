package com.afr.fms.Admin.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
   private Long id;
   private String  first_name;
   private String last_name;
   private String phone_number;
   private String email;
   private User user;  
   private String title;
   private Long registered_by;
 
}
