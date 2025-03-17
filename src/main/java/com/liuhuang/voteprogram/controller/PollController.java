package com.liuhuang.voteprogram.controller;


import com.liuhuang.voteprogram.dto.PollCreateDTO;
import com.liuhuang.voteprogram.model.Polls;
import com.liuhuang.voteprogram.response.ApiResponse;
import com.liuhuang.voteprogram.service.PollService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

@RestController
@RequestMapping("/api/polls")
@CrossOrigin(origins = "http://localhost:5173")
@Validated
public class PollController {

    private final PollService pollService;

    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    @PostMapping("/createPoll")
    public ApiResponse<Polls> createPoll(@RequestBody @Valid PollCreateDTO dto) {
        try {
            Polls polls =  pollService.createPoll(dto);
            return ApiResponse.success("投票创建成功", polls);
        } catch (ValidationException e){
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error(400, "服务器内部错误:" + e.getMessage());
        }
    }

    @GetMapping("/getActivePolls")
    public ApiResponse<List<Polls>> getActivePolls() {
        List<Polls> activePolls = pollService.getActivePolls();
        return ApiResponse.success("获取活动投票成功", activePolls);
    }
}
