package com.liuhuang.voteprogram.dto;


import lombok.Data;

@Data
public class VoteCreateDTO {

    private Long pollId;
    private Long userId;
    private Long optionId;
}
