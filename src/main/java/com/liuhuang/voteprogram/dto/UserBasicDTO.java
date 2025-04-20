package com.liuhuang.voteprogram.dto;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserBasicDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long userId;
    private String username;
    private String nickname;

    public UserBasicDTO(Long userId,String username, String nickname) {
        this.userId = userId;
        this.username = username;
        this.nickname = nickname;
    }
}
