package com.liuhuang.voteprogram.dto;


import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class PollCreateAndUpdateDTO {

    @NotBlank(message = "投票标题不能为空")
    @Size(min = 5, max = 255, message = "投票标题长度为5-255位")
    private String title;

    @Size(max = 255, message = "投票描述长度为255位")
    private String description;

    @NotBlank(message = "创建者不能为空")
    private Long creator_id;

    @NotNull(message = "投票结束时间不能为空")
    @Future
    private LocalDateTime endTime;

    @NotNull(message = "投票开始时间不能为空")
    @Future
    private LocalDateTime startTime;

    @NotBlank(message = "分类不能为空")
    private int categoryId;

    @NotBlank(message = "最大选项不能为空")
    private int maxChoice;

    @NotBlank(message = "是否匿名不能为空")
    private Boolean isAnonymous;
}
