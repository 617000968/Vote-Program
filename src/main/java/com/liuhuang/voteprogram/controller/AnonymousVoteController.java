package com.liuhuang.voteprogram.controller;

import com.liuhuang.voteprogram.dto.AnonymousVoteCountResponseDTO;
import com.liuhuang.voteprogram.dto.AnonymousVoteCreateDTO;
import com.liuhuang.voteprogram.exception.ValidationException;
import com.liuhuang.voteprogram.response.ApiResponse;
import com.liuhuang.voteprogram.service.AnonymousVoteService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/anonymousVote")
@CrossOrigin(origins = "http://localhost:5173")
@Validated
public class AnonymousVoteController {
    private final AnonymousVoteService anonymousVoteService;

    public AnonymousVoteController(AnonymousVoteService anonymousVoteService) {
        this.anonymousVoteService = anonymousVoteService;
    }

    @PostMapping("/create")
    public ApiResponse<String> createAnonymousVote(
            @RequestHeader("Device-Hash") String deviceHash,
            @RequestHeader("Token") String token,
            @RequestBody @Valid AnonymousVoteCreateDTO dto) {
        try {
            anonymousVoteService.createAnonymousVote(dto, deviceHash, token);
            return ApiResponse.success("投票成功", null);
        } catch (ValidationException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error(500, "服务器内部错误: " + e.getMessage());
        }
    }

}
