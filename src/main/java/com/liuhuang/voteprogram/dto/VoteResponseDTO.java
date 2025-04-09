package com.liuhuang.voteprogram.dto;


import lombok.Data;

@Data
public class VoteResponseDTO {
    private String username;
    private String pollName;
    private String optionText;
}