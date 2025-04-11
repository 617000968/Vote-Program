package com.liuhuang.voteprogram.dto;


import lombok.Data;

import java.util.List;

@Data
public class AnonymousVoteCreateDTO {
    private Long pollId;
    private List<Long> optionId;
}
