package com.liuhuang.voteprogram.repository;

import com.liuhuang.voteprogram.dto.OptionResponseDTO;
import com.liuhuang.voteprogram.dto.OptionWithPollDTO;
import com.liuhuang.voteprogram.model.Options;
import com.liuhuang.voteprogram.model.Polls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Size;
import java.util.List;


@Repository
public interface OptionRepository extends JpaRepository<Options, Long> {

    boolean existsByPollAndOptionText(Polls poll, @Size(max = 255) String optionText);


    @Query("SELECT new com.liuhuang.voteprogram.dto.OptionResponseDTO(" +
            "o.poll.pollId, o.optionText) " +
            "FROM Options o " +
            "WHERE o.poll = :poll")
    List<OptionResponseDTO> findByPoll(@Param("poll") Polls poll);

    @Query(value = "SELECT o.option_id, o.option_text, COUNT(v.vote_id) AS vote_count " +
            "FROM options o " +
            "LEFT JOIN votes v ON o.option_id = v.option_id " +
            "WHERE o.poll_id = :pollId " +
            "GROUP BY o.option_id, o.option_text", nativeQuery = true)
    List<Object[]> findOptionsWithVoteCountByPollId(@Param("pollId") Long pollId);


}
