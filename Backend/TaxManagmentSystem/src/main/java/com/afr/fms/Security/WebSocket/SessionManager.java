package com.afr.fms.Security.WebSocket;

import com.afr.fms.Admin.Entity.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class SessionManager {

    private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);

    private final SimpMessagingTemplate messagingTemplate;
    private final UserSessionRepository userSessionRepository;

    public SessionManager(SimpMessagingTemplate messagingTemplate,
            UserSessionRepository userSessionRepository) {
        this.messagingTemplate = messagingTemplate;
        this.userSessionRepository = userSessionRepository;
    }

    /**
     * Register a new session. Deletes ALL previous sessions for this user (active
     * or not).
     */
    public void registerSession(String username, Long trackerId) {
        if (username == null || trackerId == null) {
            throw new IllegalArgumentException("Username and tracker id must not be null");
        }

        // Delete all old sessions first
        List<UserSession> existingSessions = userSessionRepository.findByUserName(username);
        if (!existingSessions.isEmpty()) {
            logger.warn("Deleting {} previous sessions for user: {}", existingSessions.size(), username);

            try {
                messagingTemplate.convertAndSendToUser(
                        username,
                        "/queue/logout",
                        "You have been logged out due to another login.");
            } catch (Exception e) {
                logger.error("Failed to notify previous session for user: {}", username, e);
            }

            userSessionRepository.deleteAll(existingSessions);
        }

        // Save new session
        UserSession session = UserSession.builder()
                .userName(username)
                .trackerId(trackerId)
                .createdDate(LocalDateTime.now())
                .lastActive(LocalDateTime.now())
                .isActive(true) // optional now, since we delete instead of flagging
                .build();

        userSessionRepository.save(session);
    }

    /**
     * Validate if a trackerId belongs to an existing session of the user.
     */
    public boolean isTrackerValid(String username, Long trackerId) {
        return userSessionRepository.findByUserName(username).stream()
                .anyMatch(s -> s.getTrackerId().equals(trackerId));
    }

    /**
     * Remove a specific session for a user.
     */
    public void invalidateSession(String username, Long trackerId) {
        List<UserSession> sessions = userSessionRepository.findByUserName(username);
        sessions.stream()
                .filter(s -> s.getTrackerId().equals(trackerId))
                .forEach(session -> {
                    userSessionRepository.delete(session);
                    logger.debug("Deleted session - user: {}, tracker: {}", username, trackerId);
                });
    }

    /**
     * Remove ALL sessions for a user.
     */
    public void invalidateAllSessions(String username) {
        List<UserSession> sessions = userSessionRepository.findByUserName(username);
        if (!sessions.isEmpty()) {
            userSessionRepository.deleteAll(sessions);
            logger.warn("Deleted all sessions for user: {}", username);
        }
    }

    public String getUsernameForTracker(Long trackerId) {
        return userSessionRepository.findByTrackerId(trackerId)
                .map(UserSession::getUserName)
                .orElse(null);
    }
}
