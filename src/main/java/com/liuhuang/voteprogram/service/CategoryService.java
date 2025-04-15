package com.liuhuang.voteprogram.service;


import com.liuhuang.voteprogram.dto.CategoryActiveAndCountDTO;
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

    public List<CategoryActiveAndCountDTO> getAllDetailedCategory() {
        return categoryRepository.getAllDetailedCategory();
    }

    public List<CategoryDTO> getActiveCategory() {
        return categoryRepository.getActiveCategory();
    }

    public void deleteCategory(int categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    public void updateCategoryStatus(int id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ValidationException("分类不存在"));
        category.setActive(!category.isActive());
        categoryRepository.save(category);
    }
}
