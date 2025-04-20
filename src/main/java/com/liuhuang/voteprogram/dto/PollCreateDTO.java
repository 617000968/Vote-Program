package com.liuhuang.voteprogram.dto;

import lombok.Data;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PollCreateDTO {

    @NotBlank(message = "投票标题不能为空")
    @Size(min = 5, max = 255, message = "投票标题长度为5-255位")
    private String title;

    @Size(max = 255, message = "投票描述长度为255位")
    private String description;

    @NotNull(message = "创建者不能为空")
    private Long creator_id;

    @NotNull(message = "投票结束时间不能为空")
    @Future(message = "投票结束时间必须是未来的时间")
    private LocalDateTime endTime;

    @NotNull(message = "投票开始时间不能为空")
    @Future(message = "投票开始时间必须是未来的时间")
    private LocalDateTime startTime;

    @NotNull(message = "分类不能为空")
    private Integer categoryId;

    @NotNull(message = "最大选项不能为空")
    private Integer maxChoice;

    @NotNull(message = "是否匿名不能为空")
    private Boolean isAnonymous;

    @Size(min = 5, max = 10, message = "匿名代码长度必须在5到10个字符之间")
    private String anonymousCode;

    @NotNull
    @Size(min = 2)
    private List<String> options;
}
