package com.liuhuang.voteprogram.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "polls")
@NoArgsConstructor
public class Polls {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "poll_id")
    private Long pollId;

    @Column(nullable = false, unique = true, name = "title")
    private String title;

    @Column(columnDefinition = "text", length = 65535, name = "description")
    private String description;

    @Column(nullable = false, name = "start_time")
    private LocalDateTime startTime;

    @Column(nullable = false, name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "is_deleted", columnDefinition = "tinyint default 0")
    private boolean isDeleted;

    @Column(nullable = false, name = "max_choice")
    private int maxChoice;

    @Column(nullable = false, name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "is_anonymous")
    private boolean isAnonymous;

    @ManyToOne
    @JsonManagedReference("poll-category")
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    @ManyToOne
    @JsonManagedReference("poll-creator")
    @JoinColumn(name = "creator_id", referencedColumnName = "user_id")
    private User creator;
}
