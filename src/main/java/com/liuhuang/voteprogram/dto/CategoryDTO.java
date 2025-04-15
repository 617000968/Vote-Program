package com.liuhuang.voteprogram.dto;


import lombok.Data;

@Data
public class CategoryDTO {
    private int categoryId;
    private String name;

    public CategoryDTO(int categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }
}
