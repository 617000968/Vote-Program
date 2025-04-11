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



}
