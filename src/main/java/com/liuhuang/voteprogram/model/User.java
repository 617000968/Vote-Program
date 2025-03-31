package com.liuhuang.voteprogram.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String role = "USER";

    @Column(columnDefinition = "tinyint default 0")
    private boolean isDeleted;

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;

    @OneToMany(mappedBy = "creator", fetch = FetchType.EAGER)
    @JsonBackReference("poll-creator")
    private List<Polls> polls;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference("vote-user")
    private List<Votes> vote;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference("log-user")
    private List<Logs> log;
}