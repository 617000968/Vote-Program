package com.liuhuang.voteprogram.dto;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CategoryDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int categoryId;
    private String name;

    public CategoryDTO(int categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }
}
