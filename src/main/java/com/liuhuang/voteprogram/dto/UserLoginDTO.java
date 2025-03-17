package com.liuhuang.voteprogram.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
public class UserLoginDTO {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 6, max = 12, message = "用户名长度为6-12位")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 16, message = "密码长度为8-16位")
    private String password;
}
