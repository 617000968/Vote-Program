package com.liuhuang.voteprogram.dto;


import lombok.Data;

@Data
public class OptionWithPollDTO {
    private Long optionId;
    private String optionText;
    private Long voteCount;

    public OptionWithPollDTO(Long optionId, String optionText, Long voteCount) {
        this.optionId = optionId;
        this.optionText = optionText;
        this.voteCount = voteCount;
    }
}