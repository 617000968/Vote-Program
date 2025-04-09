package com.liuhuang.voteprogram.service;

import com.liuhuang.voteprogram.dto.VoteCountResponse;
import com.liuhuang.voteprogram.dto.VoteCreateDTO;
import com.liuhuang.voteprogram.dto.VoteResponseDTO;
import com.liuhuang.voteprogram.exception.ValidationException;
import com.liuhuang.voteprogram.model.Options;
import com.liuhuang.voteprogram.model.Polls;
import com.liuhuang.voteprogram.model.User;
import com.liuhuang.voteprogram.model.Votes;
import com.liuhuang.voteprogram.repository.VoteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VoteService {
    private final VoteRepository voteRepository;
    private final PollService pollService;
    private final OptionService optionService;
    private final UserService userService;

    public VoteService(VoteRepository voteRepository, PollService pollService, OptionService optionService, UserService userService) {
        this.voteRepository = voteRepository;
        this.pollService = pollService;
        this.optionService = optionService;
        this.userService = userService;
    }


    public VoteResponseDTO createVote(VoteCreateDTO dto) {
        Polls polls = pollService.getPollsByPollId(dto.getPollId());
        User user = userService.getUserById(dto.getUserId());
        Options options = optionService.getOptionById(dto.getOptionId());
        if (voteRepository.existsByPollAndUser(polls, user)) {
            throw new ValidationException("用户已经投票");
        }
        Votes votes = new Votes();
        votes.setPoll(polls);
        votes.setUser(user);
        votes.setOption(options);
        votes.setCreatedAt(LocalDateTime.now());
        VoteResponseDTO responseDTO = new VoteResponseDTO();
        responseDTO.setUsername(user.getUsername());
        responseDTO.setPollName(polls.getTitle());
        responseDTO.setOptionText(options.getOptionText());
        voteRepository.save(votes);
        return responseDTO;
    }

    public List<VoteCountResponse> getVoteCountByPollId(Long pollId) {
        Polls polls = pollService.getPollsByPollId(pollId);
        return voteRepository.getVoteCountByPoll(polls);
    }
}