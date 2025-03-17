package com.liuhuang.voteprogram.model;


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
    private int log_id;

    @Column(nullable = false)
    private Long adminId;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private Long pollId;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime actionTime;
}
