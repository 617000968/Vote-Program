package com.liuhuang.voteprogram.dto;


import lombok.Data;

@Data
public class UserBasicDTO {
    private String username;
    private String nickname;

    public UserBasicDTO(String username, String nickname) {
        this.username = username;
        this.nickname = nickname;
    }
}
