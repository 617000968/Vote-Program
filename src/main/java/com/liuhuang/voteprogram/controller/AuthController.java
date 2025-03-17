package com.liuhuang.voteprogram.controller;

import com.liuhuang.voteprogram.dto.UserRegisterDTO;
import com.liuhuang.voteprogram.exception.ValidationException;
import com.liuhuang.voteprogram.model.User;
import com.liuhuang.voteprogram.response.ApiResponse;
import com.liuhuang.voteprogram.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
@Validated
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }


    @PostMapping("/login")
    public ApiResponse<User> login(@RequestBody @Valid UserRegisterDTO dto){
        try {
            User user = userService.login(dto);
            return ApiResponse.success("登录成功", user);
        } catch (ValidationException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e){
            return ApiResponse.error(400, "服务器内部错误" + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody @Valid UserRegisterDTO dto){
        try {
            User user = userService.register(dto);
            return ApiResponse.success("注册成功", user);
        } catch (ValidationException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e){
            return ApiResponse.error(400, "服务器内部错误" + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ApiResponse<User> update(@RequestBody @Valid UserRegisterDTO dto, @PathVariable @Min(1) Long id){
        try {
            User user = userService.update(id, dto);
            return ApiResponse.success("更新成功", user);
        } catch (ValidationException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e){
            return ApiResponse.error(400, "服务器内部错误" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<User> delete(@PathVariable @Min(1) Long id){
        try {
            User user = userService.delete(id);
            return ApiResponse.success("删除成功", user);
        } catch (ValidationException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e){
            return ApiResponse.error(400, "服务器内部错误" + e.getMessage());
        }
    }
}