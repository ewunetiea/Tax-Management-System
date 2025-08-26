package com.afr.fms.Security.WebSocket;

import com.afr.fms.Admin.Entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    List<UserSession> findByUserName(String userName);   // no isActive filter

    Optional<UserSession> findByTrackerId(Long trackerId);

    void deleteByUserName(String userName); // direct bulk delete if you want
}
