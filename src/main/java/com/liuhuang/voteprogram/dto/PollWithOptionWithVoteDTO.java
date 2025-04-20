package com.liuhuang.voteprogram.dto;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PollWithOptionWithVoteDTO {
    private Long pollId;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int maxChoice;
    private LocalDateTime createdAt;
    private boolean voted;
    private boolean isAnonymous;
    private CategoryDTO categoryDTO;
    private UserBasicDTO userBasicDTO;
    private List<VoteCountResponseDTO> voteCountResponseDTO;

    public PollWithOptionWithVoteDTO(Long pollId, String title, String description,
                                     LocalDateTime startTime, LocalDateTime endTime,
                                     int maxChoice, LocalDateTime createdAt,boolean voted,
                                     boolean isAnonymous, CategoryDTO categoryDTO,
                                     UserBasicDTO userBasicDTO,
                                     List<VoteCountResponseDTO> voteCountResponseDTO) {
        this.pollId = pollId;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxChoice = maxChoice;
        this.createdAt = createdAt;
        this.voted = voted;
        this.isAnonymous = isAnonymous;
        this.categoryDTO = categoryDTO;
        this.userBasicDTO = userBasicDTO;
        this.voteCountResponseDTO = voteCountResponseDTO;
    }


}