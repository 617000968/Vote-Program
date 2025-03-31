package com.liuhuang.voteprogram.repository;

import com.liuhuang.voteprogram.dto.PollCreateAndUpdateDTO;
import com.liuhuang.voteprogram.dto.PollResponseDTO;
import com.liuhuang.voteprogram.model.Category;
import com.liuhuang.voteprogram.model.Polls;
import com.liuhuang.voteprogram.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PollRepository extends JpaRepository<Polls, Long> {

    @Query("SELECT p FROM Polls p WHERE p.title = ?1 AND p.isDeleted = false")
    Optional<Polls> findByActiveTitle(String title);
    @Query("SELECT NEW com.liuhuang.voteprogram.dto.PollResponseDTO(" +
            "p.pollId, p.title, p.description, p.startTime, p.endTime, " +
            "p.maxChoice, p.createdAt, c, u) " +
            "FROM Polls p " +
            "LEFT JOIN p.category c " +
            "LEFT JOIN p.creator u " +
            "WHERE p.isDeleted = false AND CURRENT_TIMESTAMP BETWEEN p.startTime AND p.endTime")
    List<PollResponseDTO> findActivePolls();


    @Query("SELECT NEW com.liuhuang.voteprogram.dto.PollResponseDTO(" +
            "p.pollId, p.title, p.description, p.startTime, p.endTime, " +
            "p.maxChoice, p.createdAt, c, u) " +
            "FROM Polls p " +
            "LEFT JOIN p.category c " +
            "LEFT JOIN p.creator u " +
            "WHERE p.isDeleted = false")
    List<PollResponseDTO> findAllPolls();


    @Query("SELECT COUNT(p) > 0 FROM Polls p WHERE p.title = :title AND p.isDeleted = false AND p.pollId <> :pollId")
    boolean existsByTitleAndExcludeId(String title, Long pollId);



    List<PollResponseDTO> findPollsByCreator(User creator);

    List<PollResponseDTO> findByCategory(Category category);

//    @Query("SELECT NEW com.liuhuang.voteprogram.dto.PollUpdateDTO(" +
//            "p.title, p.description, p.startTime, p.endTime," +
//            "p.maxChoice, c, u)" +
//            "FROM Polls p" +
//            "LEFT JOIN p.category c" +
//            "LEFT JOIN p.creator u" +
//            "WHERE p.pollId = :pollId AND p.isDeleted = false")
//    Optional<PollCreateAndUpdateDTO> findPollById(@Param("pollId") Long pollId);
//
//
//    @Query("SELECT p FROM Polls p LEFT JOIN FETCH p.category LEFT JOIN FETCH p.creator WHERE p.pollId = :pollId")
//    Optional<Polls> findPollWithAssociations(@Param("pollId") Long pollId);
}
