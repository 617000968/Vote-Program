package com.liuhuang.voteprogram.service;

import com.liuhuang.voteprogram.dto.VoteCountResponseDTO;
import com.liuhuang.voteprogram.dto.VoteCreateDTO;
import com.liuhuang.voteprogram.dto.VoteResponseDTO;
import com.liuhuang.voteprogram.exception.ValidationException;
import com.liuhuang.voteprogram.model.Options;
import com.liuhuang.voteprogram.model.Polls;
import com.liuhuang.voteprogram.model.User;
import com.liuhuang.voteprogram.model.Votes;
import com.liuhuang.voteprogram.repository.PollRepository;
import com.liuhuang.voteprogram.repository.VoteRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VoteService {
    private final VoteRepository voteRepository;
    private final PollService pollService;
    private final OptionService optionService;
    private final UserService userService;
    private final PollRepository pollRepository;

    public VoteService(VoteRepository voteRepository, PollService pollService,
                       OptionService optionService, UserService userService,
                       PollRepository pollRepository) {
        this.voteRepository = voteRepository;
        this.pollService = pollService;
        this.optionService = optionService;
        this.userService = userService;
        this.pollRepository = pollRepository;
    }


    @CacheEvict(value = "voteCount", key = "#dto.pollId")
    public List<VoteResponseDTO> createVote(VoteCreateDTO dto) {
        Polls polls = pollService.getPollsByPollId(dto.getPollId());
        User user = userService.getUserById(dto.getUserId());
        if (voteRepository.existsByPollAndUser(polls, user)) {
            throw new ValidationException("用户已经投票");
        }
        if (dto.getOptionId().size() > polls.getMaxChoice()) {
            throw new ValidationException("投票选项超过限制");
        }
        if (!pollRepository.existsActivePoll(polls.getPollId())){
            throw new ValidationException("投票已经结束");
        }
        if (polls.isAnonymous()) {
            throw new ValidationException("不可实名制投匿名投票！");
        }
        List<VoteResponseDTO> responseDTOS = new ArrayList<>();
        for (Long optionId : dto.getOptionId()) {
            Options options = optionService.getOptionById(optionId);
            if (!options.getPoll().getPollId().equals(dto.getPollId())) {
                throw new ValidationException("投票选项不匹配");
            }
            Votes votes = new Votes();
            votes.setPoll(polls);
            votes.setUser(user);
            votes.setOption(options);
            votes.setCreatedAt(LocalDateTime.now());
            voteRepository.save(votes);
            VoteResponseDTO responseDTO = new VoteResponseDTO();
            responseDTO.setUsername(user.getUsername());
            responseDTO.setPollName(polls.getTitle());
            responseDTO.setOptionText(options.getOptionText());
            responseDTOS.add(responseDTO);
        }
        return responseDTOS;
    }

    @Cacheable(value = "voteCount", key = "#pollId")
    public List<VoteCountResponseDTO> getVoteCountByPollId(Long pollId) {
        String redisKey = "voteCount:poll:" + pollId;

        Polls polls = pollService.getPollsByPollId(pollId);
        return voteRepository.getVoteCountByPoll(polls);
    }

    @Cacheable(value = "userVotes", key = "#userId")
    @Transactional(readOnly = true)
    public List<VoteResponseDTO> getVoteByUserId(Long userId) {
        return voteRepository.findVoteByUserId(userId);

    }

    public boolean hasVoted(Long optionId, Long userId) {
        User user = userService.getUserById(userId);
        Options options = optionService.getOptionById(optionId);
        return voteRepository.existsByUserAndOption(user, options);
    }
}