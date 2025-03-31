package com.liuhuang.voteprogram.repository;

import com.liuhuang.voteprogram.dto.OptionResponseDTO;
import com.liuhuang.voteprogram.model.Options;
import com.liuhuang.voteprogram.model.Polls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Size;
import java.util.List;


@Repository
public interface OptionRepository extends JpaRepository<Options, Long> {

    boolean existsByPollAndOptionText(Polls poll, @Size(max = 255) String optionText);

    List<OptionResponseDTO> findPollsOptions(Polls polls);
}
