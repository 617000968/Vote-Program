package com.liuhuang.voteprogram.dto;


import lombok.Data;

@Data
public class UserBasicDTO {
    private Long userId;
    private String username;
    private String nickname;

    public UserBasicDTO(Long userId,String username, String nickname) {
        this.userId = userId;
        this.username = username;
        this.nickname = nickname;
    }
}
