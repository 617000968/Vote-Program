package com.liuhuang.voteprogram.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiResponse<T> {

    private int code;
    private String msg;
    private T data;
    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> success(String msg, T data) {
        return new ApiResponse<>(200, msg, data, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> success(String msg) {
        return new ApiResponse<>(200, msg, null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(int code, String msg) {
        return new ApiResponse<>(code, msg, null, LocalDateTime.now());
    }
}
