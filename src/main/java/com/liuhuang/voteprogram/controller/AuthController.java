package com.liuhuang.voteprogram.controller;

import com.liuhuang.voteprogram.dto.UserLoginDTO;
import com.liuhuang.voteprogram.dto.UserRegisterDTO;
import com.liuhuang.voteprogram.dto.UserResponseDTO;
import com.liuhuang.voteprogram.exception.ValidationException;
import com.liuhuang.voteprogram.model.User;
import com.liuhuang.voteprogram.response.ApiResponse;
import com.liuhuang.voteprogram.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

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
    public ApiResponse<UserResponseDTO> login(@RequestBody @Valid UserLoginDTO dto){
        try {
            User user = userService.login(dto);
            UserResponseDTO responseDTO = userService.toDTO(user);
            return ApiResponse.success("登录成功", responseDTO);
        } catch (ValidationException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e){
            return ApiResponse.error(400, "服务器内部错误" + e.getMessage());
        }
    }


    @PostMapping("/verifyUser")
    public ApiResponse<String> verifyUser(@RequestParam("username") String username,
                                         @RequestParam("email") String email){
        try {
            userService.verifyUser(username, email);
            return ApiResponse.success("验证成功", "");
        } catch (ValidationException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e){
            return ApiResponse.error(400, "服务器内部错误" + e.getMessage());
        }
    }

    @PostMapping("/forgetPassword")
    public ApiResponse<String> forgetPassword(
            @RequestParam("username") String username,
            @RequestParam("password") String password){
        try {
            userService.forgetPassword(username, password);
            return ApiResponse.success("发送成功", "");
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
    public ApiResponse<UserResponseDTO> update(@RequestBody @Valid UserRegisterDTO dto, @PathVariable @Min(1) Long id){
        try {
            User user = userService.update(id, dto);
            UserResponseDTO responseDTO = userService.toDTO(user);
            return ApiResponse.success("更新成功", responseDTO);
        } catch (ValidationException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e){
            return ApiResponse.error(400, "服务器内部错误" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<UserResponseDTO> delete(@PathVariable @Min(1) Long id){
        try {
            User user = userService.delete(id);
            UserResponseDTO responseDTO = userService.toDTO(user);
            return ApiResponse.success("删除成功", responseDTO);
        } catch (ValidationException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e){
            return ApiResponse.error(400, "服务器内部错误" + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ApiResponse<List<UserResponseDTO>> getAllUsers(){
        try {
            return ApiResponse.success("获取成功", userService.getAllUsers());
        } catch (ValidationException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e){
            return ApiResponse.error(400, "服务器内部错误" + e.getMessage());
        }
    }
}