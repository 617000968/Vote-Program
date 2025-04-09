package com.liuhuang.voteprogram.repository;


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
            "c.name)" +
            "FROM Category c")
    List<CategoryDTO> getAllCategory();

    @Query("SELECT new com.liuhuang.voteprogram.dto.CategoryDTO(" +
            "c.name)" +
            "FROM Category c " +
            "WHERE c.active = true")
    List<CategoryDTO> getActiveCategory();


}
