package com.liuhuang.voteprogram.repository;

import com.liuhuang.voteprogram.dto.AnonymousVoteCountResponseDTO;
import com.liuhuang.voteprogram.model.AnonymousVote;
import com.liuhuang.voteprogram.model.Polls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AnonymousVoteRepository extends JpaRepository<AnonymousVote, Long> {

    boolean existsByPollAndDeviceHash(Polls poll, String deviceHash);



    @Query("SELECT new com.liuhuang.voteprogram.dto.AnonymousVoteCountResponseDTO(" +
            "COUNT(a), o.optionId, o.optionText) " +
            "FROM Options o LEFT JOIN o.anonymousVote a " +
            "WHERE o.poll = :polls " +
            "GROUP BY o.optionId, o.optionId ")
    List<AnonymousVoteCountResponseDTO> getAnonymousVoteCountByPoll(Polls polls);
}
