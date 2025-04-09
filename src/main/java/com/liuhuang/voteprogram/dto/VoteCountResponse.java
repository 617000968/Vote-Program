package com.liuhuang.voteprogram.dto;


import lombok.Data;

@Data
public class VoteCountResponse {
    private String optionText;
    private Long count;

    public VoteCountResponse(String optionText, Long count) {
        this.optionText = optionText;
        this.count = count;
    }
}
