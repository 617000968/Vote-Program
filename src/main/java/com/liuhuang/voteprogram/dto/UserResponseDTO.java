package com.liuhuang.voteprogram.dto;


import lombok.Data;

@Data
public class UserResponseDTO {
    private Long userId;
    private String username;
    private String nickname;
    private String email;
    private String role;
    private boolean isDeleted;
}
