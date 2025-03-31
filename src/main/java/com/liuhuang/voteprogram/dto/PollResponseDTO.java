package com.liuhuang.voteprogram.dto;


import com.liuhuang.voteprogram.model.Category;
import com.liuhuang.voteprogram.model.User;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PollResponseDTO {
    private Long pollId;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int maxChoice;
    private LocalDateTime createdAt;
    private CategoryDTO categoryDTO;
    private UserBasicDTO userBasicDTO;
    public PollResponseDTO(Long pollId, String title, String description,
                           LocalDateTime startTime, LocalDateTime endTime,
                           int maxChoice, LocalDateTime createdAt,
                           Category category, User creator) {
        this.pollId = pollId;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxChoice = maxChoice;
        this.createdAt = createdAt;
        // 字段赋值
        this.categoryDTO = new CategoryDTO( category.getName());
        this.userBasicDTO = new UserBasicDTO(creator.getUsername(), creator.getNickname());
    }
}

