package com.liuhuang.voteprogram.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Table(name = "logs")
@Entity
@NoArgsConstructor
public class Logs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private int logId;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference("log-user")
    @JoinColumn(name = "admin_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference("log-poll")
    @JoinColumn(name = "poll_id", referencedColumnName = "poll_id")
    private Polls poll;
}
