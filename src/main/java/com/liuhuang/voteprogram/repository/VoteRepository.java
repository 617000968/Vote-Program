package com.liuhuang.voteprogram.repository;

import com.liuhuang.voteprogram.dto.VoteCountResponse;
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


    @Query("SELECT new com.liuhuang.voteprogram.dto.VoteCountResponse( " +
            "v.option.optionText, COUNT(v)) " +
            "FROM Votes v " +
            "WHERE v.poll = :polls " +
            "GROUP BY v.option.optionText")
    List<VoteCountResponse> getVoteCountByPoll(@Param("polls") Polls polls);
}
