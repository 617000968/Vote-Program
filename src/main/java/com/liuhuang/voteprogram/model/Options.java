package com.liuhuang.voteprogram.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Table(name = "options")
@Entity
@NoArgsConstructor
public class Options {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private int optionId;

    @Column(nullable = false, name = "option_text")
    @Size(max = 255)
    private String optionText;

    @Column(name = "vote_count")
    private Long voteCount;

    @Column(nullable = false, name = "created_at")
    @CreationTimestamp
    private LocalDateTime createAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference("option-poll")
    @JoinColumn(name = "poll_id", referencedColumnName = "poll_id")
    private Polls poll;
}
