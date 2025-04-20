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
            @RequestBody @Valid AnonymousVoteCreateDTO dto) {
        try {
            System.out.println(dto + deviceHash);
            anonymousVoteService.createAnonymousVote(dto, deviceHash);
            return ApiResponse.success("投票成功", null);
        } catch (ValidationException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error(500, "服务器内部错误: " + e.getMessage());
        }
    }

    @GetMapping("/{pollId}")
    public ApiResponse<List<AnonymousVoteCountResponseDTO>> getAnonymousVoteCountByPollId(
            @PathVariable Long pollId) {
        try {
            List<AnonymousVoteCountResponseDTO> anonymousVoteCountResponseDTOList =
                    anonymousVoteService.getAnonymousVoteCountByPollId(pollId);
            return ApiResponse.success("获取投票结果成功", anonymousVoteCountResponseDTOList);
        } catch (ValidationException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e){
            return ApiResponse.error(400, "服务器内部错误" + e.getMessage());
        }
    }
}
