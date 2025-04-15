package com.liuhuang.voteprogram.repository;


import com.liuhuang.voteprogram.dto.CategoryActiveAndCountDTO;
import com.liuhuang.voteprogram.dto.CategoryDTO;
import com.liuhuang.voteprogram.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByName(String name);


    @Query("SELECT new com.liuhuang.voteprogram.dto.CategoryDTO(" +
            "c.categoryId, c.name)" +
            "FROM Category c " +
            "WHERE c.active = true")
    List<CategoryDTO> getActiveCategory();

    @Query("SELECT new com.liuhuang.voteprogram.dto.CategoryActiveAndCountDTO(" +
            "c.categoryId, c.name, c.active, COUNT(p)) " +
            "FROM Category c " +
            "LEFT JOIN c.polls p " +
            "on p.isDeleted = false " +
            "GROUP BY c.categoryId, c.name, c.active ")
    List<CategoryActiveAndCountDTO> getAllDetailedCategory();
}
