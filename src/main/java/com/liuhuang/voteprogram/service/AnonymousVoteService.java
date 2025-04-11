package com.liuhuang.voteprogram.service;

import com.liuhuang.voteprogram.dto.AnonymousVoteCountResponseDTO;
import com.liuhuang.voteprogram.dto.AnonymousVoteCreateDTO;
import com.liuhuang.voteprogram.exception.ValidationException;
import com.liuhuang.voteprogram.model.AnonymousVote;
import com.liuhuang.voteprogram.model.Options;
import com.liuhuang.voteprogram.model.Polls;
import com.liuhuang.voteprogram.repository.AnonymousVoteRepository;
import com.liuhuang.voteprogram.repository.OptionRepository;
import com.liuhuang.voteprogram.repository.PollRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AnonymousVoteService {
    private final AnonymousVoteRepository anonymousVoteRepository;
    private final PollRepository pollRepository;
    private final OptionRepository optionRepository;

    public AnonymousVoteService(AnonymousVoteRepository anonymousVoteRepository,
                                PollRepository pollRepository, OptionRepository optionRepository) {
        this.anonymousVoteRepository = anonymousVoteRepository;
        this.pollRepository = pollRepository;
        this.optionRepository = optionRepository;
    }


    public void createAnonymousVote(AnonymousVoteCreateDTO dto, String deviceHash, String token) {
        Polls poll = pollRepository.findById(dto.getPollId())
                .orElseThrow(() -> new ValidationException("投票不存在"));

        if (!isPollActive(poll)) {
            throw new ValidationException("投票已结束");
        }
        if (!poll.isAnonymous()) {
            throw new ValidationException("不可匿名投实名投票！");
        }
        if (dto.getOptionId().size() > poll.getMaxChoice()) {
            throw new ValidationException("不可超过最大选择数");
        }
        if (anonymousVoteRepository.existsByPollAndDeviceHash(poll, deviceHash)) {
            throw new ValidationException("该设备已投过票");
        }
        for (Long optionId : dto.getOptionId()) {
            Options option = optionRepository.findById(optionId)
                    .orElseThrow(() -> new ValidationException("选项不存在"));

            if (!option.getPoll().equals(poll)) {
                throw new ValidationException("选项不属于该投票");
            }


            AnonymousVote vote = new AnonymousVote();
            vote.setPoll(poll);
            vote.setOption(option);
            vote.setDeviceHash(deviceHash);
            vote.setToken(token);
            anonymousVoteRepository.save(vote);
        }
    }

    private boolean isPollActive(Polls poll) {
        LocalDateTime now = LocalDateTime.now();
        return !poll.isDeleted() && now.isAfter(poll.getStartTime()) && now.isBefore(poll.getEndTime());
    }


}
