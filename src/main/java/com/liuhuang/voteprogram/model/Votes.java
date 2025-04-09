package com.liuhuang.voteprogram.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "votes")
@NoArgsConstructor
public class Votes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private Long voteId;

    @Column(nullable = false, name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference("vote-poll")
    @JoinColumn(name = "poll_id", referencedColumnName = "poll_id")
    private Polls poll;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference("vote-user")
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference("vote-option")
    @JoinColumn(name = "option_id", referencedColumnName = "option_id")
    private Options option;

}
