package com.liuhuang.voteprogram.dto;


import com.liuhuang.voteprogram.model.Category;
import com.liuhuang.voteprogram.model.User;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class PollResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long pollId;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int maxChoice;
    private LocalDateTime createdAt;
    private boolean isAnonymous;
    private CategoryDTO categoryDTO;
    private UserBasicDTO userBasicDTO;
    public PollResponseDTO(Long pollId, String title, String description,
                           LocalDateTime startTime, LocalDateTime endTime,
                           int maxChoice, LocalDateTime createdAt, boolean isAnonymous,
                           Category category, User creator) {
        this.pollId = pollId;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxChoice = maxChoice;
        this.createdAt = createdAt;
        this.isAnonymous = isAnonymous;
        // 字段赋值
        this.categoryDTO = new CategoryDTO(category.getCategoryId(), category.getName());
        this.userBasicDTO = new UserBasicDTO(creator.getUserId(), creator.getUsername(), creator.getNickname());
    }
}

