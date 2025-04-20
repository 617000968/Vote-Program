package com.liuhuang.voteprogram.repository;

import com.liuhuang.voteprogram.dto.VoteCountResponseDTO;
import com.liuhuang.voteprogram.dto.VoteResponseDTO;
import com.liuhuang.voteprogram.model.Options;
import com.liuhuang.voteprogram.model.Polls;
import com.liuhuang.voteprogram.model.User;
import com.liuhuang.voteprogram.model.Votes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Votes, Long> {
    Boolean existsByPollAndUser(Polls polls, User user);

    Boolean existsByPoll(Polls polls);


    @Query("SELECT new com.liuhuang.voteprogram.dto.VoteCountResponseDTO( " +
            "o.optionText, COUNT(v), o.optionId) " +
            "FROM Options o LEFT JOIN o.vote v " +
            "WHERE o.poll = :polls " +
            "GROUP BY o.optionText, o.optionId")
    List<VoteCountResponseDTO> getVoteCountByPoll(@Param("polls") Polls polls);



    boolean existsByUserAndOption(User user, Options option);

    @Query("SELECT new com.liuhuang.voteprogram.dto.VoteResponseDTO( " +
            "v.user.username, v.poll.title, v.option.optionText) " +
            "FROM Votes v " +
            "WHERE v.user.userId = :userId ")
    List<VoteResponseDTO> findVoteByUserId(Long userId);
}
