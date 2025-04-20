package com.liuhuang.voteprogram.config;


import com.liuhuang.voteprogram.service.CategoryService;
import com.liuhuang.voteprogram.service.PollService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

@Configuration
public class CacheInitializer {


    @Autowired
    private PollService pollService;

    @Autowired
    private CategoryService categoryService;

    @PostConstruct
    public void preLoadCache() {
        loadCriticalData();
        loadSecondaryData();
    }


    private void loadCriticalData() {
        pollService.getActivePolls();
    }

    private void loadSecondaryData() {
        Executors.newSingleThreadExecutor().submit( () -> {
            categoryService.getActiveCategory();
            categoryService.getAllDetailedCategory();
        });
    }

}
