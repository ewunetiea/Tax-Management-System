package com.afr.fms.Common.UserSession.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Common.UserSession.Entity.UserSession;
import com.afr.fms.Common.UserSession.Mapper.ConcruntUserCheckMapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service

public class ConcrunetUserService {
    

    @Autowired
private ConcruntUserCheckMapper userSessionMapper;


public UserSession getUserSessionById(Long id) {

    UserSession session = userSessionMapper.getUserSessionById(id);

    return session;
}


    public void saveUpdate(UserSession userSession)
    {

    if (userSessionMapper.sessionExists(userSession.getUser_id()) > 0) {
        userSessionMapper.updateSession(userSession);
    } else {
        userSessionMapper.insertSession(userSession);
    }
}


    
}
