package com.liuhuang.voteprogram.dto;


import lombok.Data;

@Data
public class VoteCountResponseDTO {
    private String optionText;
    private Long count;
    private int optionId;
    public VoteCountResponseDTO(String optionText, Long count, int optionId) {
        this.optionText = optionText;
        this.count = count;
        this.optionId = optionId;
    }
}
