package com.liuhuang.voteprogram.service;


import com.liuhuang.voteprogram.dto.CategoryDTO;
import com.liuhuang.voteprogram.exception.ValidationException;
import com.liuhuang.voteprogram.model.Category;
import com.liuhuang.voteprogram.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Object createCategory(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new ValidationException("分类已存在");
        }
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    public List<CategoryDTO> getAllCategory() {
        return categoryRepository.getAllCategory();
    }
}
