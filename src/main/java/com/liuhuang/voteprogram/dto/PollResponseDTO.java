package com.liuhuang.voteprogram.dto;


import lombok.Data;

@Data
public class PollResponseDTO {
    private Long pollId;
    private String title;
    private String description;
    private CategoryDTO categoryDTO;
    private GetUserNameAndIdDTO getUserNameAndIdDTO;
}

