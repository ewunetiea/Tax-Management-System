package com.tms.Admin.Entity;

import lombok.*;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "user_session")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "tracker_id", nullable = false, unique = true)
    private Long trackerId;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "last_active")
    private LocalDateTime lastActive;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
