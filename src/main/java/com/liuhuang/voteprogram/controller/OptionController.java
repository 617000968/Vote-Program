package com.liuhuang.voteprogram.controller;


import com.liuhuang.voteprogram.dto.OptionResponseDTO;
import com.liuhuang.voteprogram.exception.ValidationException;
import com.liuhuang.voteprogram.response.ApiResponse;
import com.liuhuang.voteprogram.service.OptionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/options")
@CrossOrigin(origins = "http://localhost:5173")
@Validated
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping("/create")
    public ApiResponse<OptionResponseDTO> createOption(@RequestBody @Valid OptionResponseDTO dto) {
        try {
            OptionResponseDTO options = optionService.createOption(dto);
            return ApiResponse.success("创建成功", options);
        } catch (ValidationException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error(400, "服务器内部错误" + e.getMessage());
        }
    }

    @PatchMapping("/update/{optionId}")
    public ApiResponse<OptionResponseDTO> updateOption(@PathVariable Long optionId,
                                                       @RequestBody OptionResponseDTO dto) {
        try{
            OptionResponseDTO option = optionService.updateOption(optionId, dto);
            return ApiResponse.success("更新成功", option);
        } catch (ValidationException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e){
            return ApiResponse.error(400, "服务器内部错误" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{optionId}")
    public ApiResponse<?> deleteOption(@PathVariable Long optionId) {
        try{
            optionService.deleteOption(optionId);
            return ApiResponse.success("删除成功", null);
        } catch (ValidationException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error(400, "服务器内部错误" + e.getMessage());
        }
    }

    @GetMapping("/poll/{pollId}")
    public ApiResponse<List<OptionResponseDTO>> getPollOptions(@PathVariable Long pollId) {
        List<OptionResponseDTO> optionResponseDTOS = optionService.getPollOptions(pollId);
        return ApiResponse.success("获取投票选项成功", optionResponseDTOS);
    }
}
