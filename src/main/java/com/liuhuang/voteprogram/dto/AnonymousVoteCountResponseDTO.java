package com.liuhuang.voteprogram.dto;


import lombok.Data;

@Data
public class AnonymousVoteCountResponseDTO {
    private Long count;
    private int optionId;
    private String optionText;

    public AnonymousVoteCountResponseDTO(Long count, int optionId, String optionText) {
        this.count = count;
        this.optionId = optionId;
        this.optionText = optionText;
    }
}
