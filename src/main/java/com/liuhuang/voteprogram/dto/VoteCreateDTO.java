package com.liuhuang.voteprogram.dto;


import lombok.Data;

import java.util.List;

@Data
public class VoteCreateDTO {

    private Long pollId;
    private Long userId;
    private List<Long> optionId;
}
