package com.liuhuang.voteprogram.repository;

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

    @Query("SELECT CASE WHEN COUNT(p) > 0 " +
            "THEN true " +
            "ELSE false END " +
            "FROM Polls p " +
            "WHERE p.pollId = :pollId " +
            "AND p.isDeleted = false " +
            "AND CURRENT_TIMESTAMP BETWEEN p.startTime AND p.endTime ")
    boolean existsActivePoll(@Param("pollId") Long pollId);

    @Query("SELECT NEW com.liuhuang.voteprogram.dto.PollResponseDTO(" +
            "p.pollId, p.title, p.description, p.startTime, p.endTime, " +
            "p.maxChoice, p.createdAt, c, u) " +
            "FROM Polls p " +
            "LEFT JOIN p.category c " +
            "LEFT JOIN p.creator u " +
            "WHERE p.isDeleted = false")
    List<PollResponseDTO> findAllPolls();


    @Query("SELECT COUNT(p) > 0 " +
            "FROM Polls p " +
            "WHERE p.title = :title AND p.isDeleted = false AND p.pollId <> :pollId")
    boolean existsByTitleAndExcludeId(String title, Long pollId);



    @Query("SELECT NEW com.liuhuang.voteprogram.dto.PollResponseDTO(" +
            "p.pollId, p.title, p.description, p.startTime, p.endTime," +
            "p.maxChoice, p.createdAt, c, u)" +
            "FROM Polls p " +
            "LEFT JOIN p.category c " +
            "LEFT JOIN p.creator u " +
            "WHERE p.isDeleted = false AND p.creator = :creator")
    List<PollResponseDTO> findPollsByCreator(User creator);


    @Query("SELECT NEW com.liuhuang.voteprogram.dto.PollResponseDTO(" +
            "p.pollId, p.title, p.description, p.startTime, p.endTime," +
            "p.maxChoice, p.createdAt, c, u)" +
            "FROM Polls p " +
            "LEFT JOIN p.category c " +
            "LEFT JOIN p.creator u " +
            "WHERE p.isDeleted = false AND p.category = :category")
    List<PollResponseDTO> findByCategory(Category category);


    @Query("SELECT NEW com.liuhuang.voteprogram.dto.PollResponseDTO(" +
            "p.pollId, p.title, p.description, p.startTime, p.endTime, " +
            "p.maxChoice, p.createdAt, c, u ) " +
            "FROM Polls p " +
            "LEFT JOIN p.category c " +
            "LEFT JOIN p.creator u " +
            "WHERE p.isDeleted = false AND p.pollId = :pollId")
    PollResponseDTO findPollBasicInfoByPollId(Long pollId);

//    @Query("SELECT NEW com.liuhuang.voteprogram.dto.PollWithOptionsDTO(" +
//            "p.pollId, p.title, p.description, p.startTime, p.endTime, " +
//            "p.maxChoice, p.createdAt, c, u, o ) " +
//            "FROM Polls p " +
//            "LEFT JOIN p.category c " +
//            "LEFT JOIN p.creator u" +
//            "LEFT JOIN p.options o " +
//            "WHERE p.pollId = :pollId")
//    List<PollWithOptionsDTO> findPollWithOptionsByPollId(Long pollId);


}
