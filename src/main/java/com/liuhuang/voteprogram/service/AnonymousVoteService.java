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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AnonymousVoteService {
    private final AnonymousVoteRepository anonymousVoteRepository;
    private final PollRepository pollRepository;
    private final OptionRepository optionRepository;
    private final PollService pollService;
    private final RedisTemplate<String, String> redisTemplate;

    public AnonymousVoteService(AnonymousVoteRepository anonymousVoteRepository,
                                PollRepository pollRepository,
                                OptionRepository optionRepository,
                                PollService pollService, RedisTemplate<String, String> redisTemplate) {
        this.anonymousVoteRepository = anonymousVoteRepository;
        this.pollRepository = pollRepository;
        this.optionRepository = optionRepository;
        this.pollService = pollService;
        this.redisTemplate = redisTemplate;
    }


    @CacheEvict(value = "anonymousVoteCounts", key = "#dto.pollId")
    public void createAnonymousVote(AnonymousVoteCreateDTO dto, String deviceHash) {
        String lockKey = "vote_lock" + dto.getPollId() + ":" + deviceHash;
        try {
            Boolean locked =
                    redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", Duration.ofSeconds(10));
            if (Boolean.FALSE.equals(locked)) {
                throw new ValidationException("投票过于频繁，请稍后重试");
            }
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
                anonymousVoteRepository.save(vote);
            }
        } finally {
            redisTemplate.delete(lockKey);
        }
    }

    private boolean isPollActive(Polls poll) {
        LocalDateTime now = LocalDateTime.now();
        return !poll.isDeleted() && now.isAfter(poll.getStartTime()) && now.isBefore(poll.getEndTime());
    }


    @Cacheable(value = "anonymousVoteCounts", key = "#pollId")
    public List<AnonymousVoteCountResponseDTO> getAnonymousVoteCountByPollId(Long pollId) {
        Polls polls = pollService.getPollsByPollId(pollId);
        return anonymousVoteRepository.getAnonymousVoteCountByPoll(polls);
    }
}
