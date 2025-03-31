package com.liuhuang.voteprogram.controller;


import com.liuhuang.voteprogram.dto.CategoryDTO;
import com.liuhuang.voteprogram.exception.ValidationException;
import com.liuhuang.voteprogram.response.ApiResponse;
import com.liuhuang.voteprogram.service.CategoryService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
@CrossOrigin(origins = "http://localhost:5173")
@Validated
public class CategoryController {

    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService)
    {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    public ApiResponse<String> createCategory(String name) {
        try {
            return ApiResponse.success("创建成功", categoryService.createCategory(name).toString());
        } catch (ValidationException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error(400, "服务器内部错误:" + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ApiResponse<List<CategoryDTO>> getAllCategory(){
        List<CategoryDTO> categories = categoryService.getAllCategory();
        return ApiResponse.success("获取所有分类成功", categories);
    }
}
