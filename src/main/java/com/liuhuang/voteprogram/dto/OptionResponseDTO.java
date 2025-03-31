package com.liuhuang.voteprogram.dto;


import lombok.Data;

@Data
public class OptionResponseDTO {
    private Long pollId;
    private String optionText;

    public OptionResponseDTO(Long pollId, String optionText) {
        this.pollId = pollId;
        this.optionText = optionText;
    }
}