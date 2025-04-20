package com.liuhuang.voteprogram.dto;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class VoteResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String username;
    private String pollName;
    private String optionText;

    public VoteResponseDTO(String username, String pollName, String optionText) {
        this.username = username;
        this.pollName = pollName;
        this.optionText = optionText;
    }

    public VoteResponseDTO() {

    }
}