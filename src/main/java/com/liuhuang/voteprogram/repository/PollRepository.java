package com.liuhuang.voteprogram.repository;

import com.liuhuang.voteprogram.model.Polls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PollRepository extends JpaRepository<Polls, Long> {

    @Query("SELECT p FROM Polls p WHERE p.title = ?1 AND p.isDeleted = false")
    Optional<Polls> findByActiveTitle(String title);
    @Query("SELECT p FROM Polls p LEFT JOIN FETCH p.category LEFT JOIN FETCH p.creator WHERE CURRENT_TIMESTAMP BETWEEN p.startTime AND p.endTime")
    List<Polls> findActivePolls();
}
