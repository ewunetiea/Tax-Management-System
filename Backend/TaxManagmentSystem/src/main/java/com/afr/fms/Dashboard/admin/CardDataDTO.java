package com.afr.fms.Dashboard.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor

@NoArgsConstructor
public class CardDataDTO {
    private int IS_Count;
    private int MGT_Count;
    private int BFA_Count;
    private int INS_Count;
    private int LoggedIn;
    private int LoggedOut;
    
    // getters & setters
}
