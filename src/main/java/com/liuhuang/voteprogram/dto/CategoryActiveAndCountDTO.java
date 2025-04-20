package com.liuhuang.voteprogram.dto;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CategoryActiveAndCountDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
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