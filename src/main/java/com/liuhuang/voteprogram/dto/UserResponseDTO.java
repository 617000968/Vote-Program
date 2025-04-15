package com.liuhuang.voteprogram.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponseDTO {
    private Long userId;
    private String username;
    private String nickname;
    private String email;
    private String role;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private boolean isDeleted;
}
