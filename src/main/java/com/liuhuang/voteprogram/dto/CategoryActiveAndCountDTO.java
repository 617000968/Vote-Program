package com.liuhuang.voteprogram.dto;


import lombok.Data;

@Data
public class CategoryActiveAndCountDTO {
    private int categoryId;
    private String name;
    private boolean active;
    private long count;
    public CategoryActiveAndCountDTO(int categoryId, String name, boolean active, long count) {
        this.categoryId = categoryId;
        this.name = name;
        this.active = active;
        this.count = count;
    }
}