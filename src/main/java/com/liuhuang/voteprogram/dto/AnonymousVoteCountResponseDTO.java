package com.liuhuang.voteprogram.dto;


import lombok.Data;

@Data
public class AnonymousVoteCountResponseDTO {
    private Long count;
    private Long optionId;
    private String optionText;

    public AnonymousVoteCountResponseDTO(Long count, Long optionId, String optionText) {
        this.count = count;
        this.optionId = optionId;
        this.optionText = optionText;
    }
}
