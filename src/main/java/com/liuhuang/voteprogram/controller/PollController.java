package com.liuhuang.voteprogram.controller;


import com.liuhuang.voteprogram.dto.PollCreateAndUpdateDTO;
import com.liuhuang.voteprogram.dto.PollResponseDTO;
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

    @PostMapping("/create")
    public ApiResponse<PollCreateAndUpdateDTO> createPoll(@RequestBody @Valid PollCreateAndUpdateDTO dto) {
        try {
            PollCreateAndUpdateDTO polls =  pollService.createPoll(dto);
            return ApiResponse.success("投票创建成功", polls);
        } catch (com.liuhuang.voteprogram.exception.ValidationException e){
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error(400, "服务器内部错误:" + e.getMessage());
        }
    }

    @GetMapping("/active")
    public ApiResponse<List<PollResponseDTO>> getActivePolls() {
        List<PollResponseDTO> activePolls = pollService.getActivePolls();
        return ApiResponse.success("获取活动投票成功", activePolls);
    }

    @GetMapping("/all")
    public ApiResponse<List<PollResponseDTO>> getAllPolls(){
        List<PollResponseDTO> allPolls = pollService.getAllPolls();
        return ApiResponse.success("获取所有投票成功", allPolls);
    }


    @GetMapping("/user/{userId}")
    public ApiResponse<List<PollResponseDTO>> getUserPolls(@PathVariable Long userId) {
        List<PollResponseDTO> userPolls = pollService.getUserPolls(userId);
        return ApiResponse.success("获取用户投票成功", userPolls);
    }

    @GetMapping("/category/{categoryId}")
    public ApiResponse<List<PollResponseDTO>> getCategoryPolls(@PathVariable int categoryId) {
        List<PollResponseDTO> categoryPolls = pollService.getCategoryPolls(categoryId);
        return ApiResponse.success("获取分类投票成功", categoryPolls);
    }

    @PatchMapping("/update/{pollId}")
    public ApiResponse<PollCreateAndUpdateDTO> updatePoll(@PathVariable Long pollId,
                                     @RequestBody PollCreateAndUpdateDTO dto
            /*Authentication authentication **/){
        try {
//            if (authentication == null) {
//                return ApiResponse.error(401, "未登录");
//            }
            PollCreateAndUpdateDTO updatePoll = pollService.updatePolls(pollId, dto);
            return ApiResponse.success("更新投票成功", updatePoll);
        } catch (com.liuhuang.voteprogram.exception.ValidationException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error(400, "服务器内部错误:" + e.getMessage());
        }

    }

    @DeleteMapping("/delete/{pollId}")
    public ApiResponse<?> deletePoll(@PathVariable Long pollId) {
        try {
            pollService.deletePoll(pollId);
            return ApiResponse.success("删除投票成功");
        } catch (com.liuhuang.voteprogram.exception.ValidationException e){
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e){
            return ApiResponse.error(400, "服务器内部错误:" + e.getMessage());
        }
    }
}
