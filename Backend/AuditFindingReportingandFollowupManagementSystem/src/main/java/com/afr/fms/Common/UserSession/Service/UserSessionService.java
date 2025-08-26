package com.afr.fms.Common.UserSession.Service;

import com.afr.fms.Common.UserSession.Entity.UserSession;
import com.afr.fms.Common.UserSession.Mapper.UserSessionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.util.List;

@Service
public class UserSessionService {

    @Autowired
    private UserSessionMapper userSessionMapper;

   
    @Transactional
    public void invalidateSession(String username, String jwt_token) {
        userSessionMapper.invalidateSession(username, jwt_token);
    }

    @Transactional
    public void invalidateAllSessions(String username) {
        userSessionMapper.invalidateAllSessions(username);
    }

    public List<UserSession> getUserSessions(String username) {
        return userSessionMapper.findByUsername(username);
    }

    public List<UserSession> getActiveSessions(String username) {
        return userSessionMapper.findActiveSessionsByUsername(username);
    }

    public boolean isUserAlreadyLoggedIn(String username) {
        return !userSessionMapper.findActiveSessionsByUsername(username).isEmpty();
    }

    public UserSession getUserSessionByUsername(String username) {
        return userSessionMapper.getUserSessionByUsername(username);
    }

    public UserSession getUserSessionByUsernameAndToken(String username, String jwt_token) {
        return userSessionMapper.getUserSessionByUsernameAndToken(username, jwt_token);
    }





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

