package com.liuhuang.voteprogram.service;


import com.liuhuang.voteprogram.dto.OptionResponseDTO;
import com.liuhuang.voteprogram.exception.ValidationException;
import com.liuhuang.voteprogram.model.Options;
import com.liuhuang.voteprogram.model.Polls;
import com.liuhuang.voteprogram.repository.OptionRepository;
import com.liuhuang.voteprogram.repository.PollRepository;
import com.liuhuang.voteprogram.repository.VoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final PollRepository pollRepository;
    private final VoteRepository voteRepository;

    public OptionService(OptionRepository optionRepository, PollRepository pollRepository, VoteRepository voteRepository) {
        this.optionRepository = optionRepository;
        this.pollRepository = pollRepository;
        this.voteRepository = voteRepository;
    }


    public OptionResponseDTO createOption(OptionResponseDTO dto) {
        existPoll(dto);
        Options options = new Options();
        Polls polls = pollRepository.findById(dto.getPollId())
                .orElseThrow(() -> new ValidationException("投票不存在"));
        if (voteRepository.existsByPoll(polls)) {
            throw new ValidationException("投票已经投票,无法修改");
        }
        options.setOptionText(dto.getOptionText());
        options.setPoll(polls);
        optionRepository.save(options);
        return dto;
    }

    private void existPoll(OptionResponseDTO dto) {
        Polls polls = pollRepository.findById(dto.getPollId())
                .orElseThrow(() -> new ValidationException("投票不存在"));
        if (optionRepository.existsByPollAndOptionText(polls, dto.getOptionText())){
            throw new ValidationException("选项已存在");
        }
        if (!pollRepository.existsById(dto.getPollId())){
            throw new ValidationException("投票不存在");
        }
    }

    public OptionResponseDTO updateOption(Long optionId, OptionResponseDTO dto) {
        existPoll(dto);
        Options options = optionRepository.findById(optionId)
                .orElseThrow(() -> new ValidationException("选项不存在"));
        Polls polls = pollRepository.findById(dto.getPollId())
                .orElseThrow(() -> new ValidationException("投票不存在"));
        if (voteRepository.existsByPoll(polls)) {
            throw new ValidationException("投票已经投票,无法修改");
        }
        options.setOptionText(dto.getOptionText());
        optionRepository.save(options);
        return dto;
    }

    public void deleteOption(Long optionId) {
        if (!optionRepository.existsById(optionId)) {
            throw new ValidationException("选项不存在");
        }

        optionRepository.deleteById(optionId);
        optionRepository.flush();
        pollRepository.flush();
    }


    public List<OptionResponseDTO> getPollOptions(Long pollId) {
        Polls polls = pollRepository.findById(pollId)
                .orElseThrow(() -> new ValidationException("投票不存在"));
        List<OptionResponseDTO> optionResponseDTOS = optionRepository.findByPoll(polls);
        if (optionResponseDTOS.isEmpty()) {
            throw new ValidationException("投票下不存在选项");
        }
        return optionResponseDTOS;
    }

    public Options getOptionById(Long optionId) {
        return optionRepository.findById(optionId)
                .orElseThrow(() -> new ValidationException("选项不存在"));
    }
}
