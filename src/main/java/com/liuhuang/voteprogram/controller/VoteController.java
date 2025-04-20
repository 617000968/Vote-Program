package com.liuhuang.voteprogram.controller;


import com.liuhuang.voteprogram.dto.VoteCountResponseDTO;
import com.liuhuang.voteprogram.dto.VoteCreateDTO;
import com.liuhuang.voteprogram.dto.VoteResponseDTO;
import com.liuhuang.voteprogram.exception.ValidationException;
import com.liuhuang.voteprogram.response.ApiResponse;
import com.liuhuang.voteprogram.service.VoteService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/votes")
@CrossOrigin(origins = "http://localhost:5173")
@Validated
public class VoteController {
    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }


    @PostMapping("/create")
    public ApiResponse<List<VoteResponseDTO>> createVote(@RequestBody @Valid VoteCreateDTO dto) {
        try {
            System.out.println(dto);
            List<VoteResponseDTO> vote = voteService.createVote(dto);
            return ApiResponse.success("投票成功", vote);
        } catch (ValidationException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e){
            return ApiResponse.error(400, "服务器内部错误" + e.getMessage());
        }
    }

    @GetMapping("/{pollId}")
    public ApiResponse<List<VoteCountResponseDTO>> getVoteCountByPollId(@PathVariable Long pollId) {
        try {
            List<VoteCountResponseDTO> voteCount = voteService.getVoteCountByPollId(pollId);
            return ApiResponse.success("获取投票数成功", voteCount);
        } catch (ValidationException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e){
            return ApiResponse.error(400, "服务器内部错误" + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<VoteResponseDTO>> getVoteByUserId(@PathVariable Long userId) {
        try {
            List<VoteResponseDTO> vote = voteService.getVoteByUserId(userId);
            return ApiResponse.success("获取投票记录成功", vote);
        } catch (ValidationException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e){
            return ApiResponse.error(400, "服务器内部错误" + e.getMessage());
        }
    }

    @GetMapping("/exists")
    public ApiResponse<Boolean> hasVoted(@RequestParam Long optionId,
                                         @RequestParam Long userId) {
        boolean voted = voteService.hasVoted(optionId, userId);
        return ApiResponse.success("获取投票记录成功", voted);
    }
}
