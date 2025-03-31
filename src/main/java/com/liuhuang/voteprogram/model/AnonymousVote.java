package com.liuhuang.voteprogram.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data @Entity @Table(name = "anonymous_vote") @NoArgsConstructor
public class AnonymousVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private Long voteId;

    @Column(nullable = false, name = "device_hash")
    private String deviceHash;

    @Column(nullable = false, name = "token")
    private String token;

    @Column(nullable = false, name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference("anonymousVote-poll")
    @JoinColumn(name = "poll_id", referencedColumnName = "poll_id")
    private Polls poll;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference("anonymousVote-option")
    @JoinColumn(name = "option_id", referencedColumnName = "option_id")
    private Options option;
}
