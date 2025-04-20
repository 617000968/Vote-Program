package com.liuhuang.voteprogram.service;


import com.liuhuang.voteprogram.dto.*;
import com.liuhuang.voteprogram.exception.ValidationException;
import com.liuhuang.voteprogram.model.Category;
import com.liuhuang.voteprogram.model.Options;
import com.liuhuang.voteprogram.model.Polls;
import com.liuhuang.voteprogram.model.User;
import com.liuhuang.voteprogram.repository.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PollService {

    private final PollRepository pollRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final VoteRepository voteRepository;
    private final OptionRepository optionRepository;

    public PollService(PollRepository pollRepository, UserRepository userRepository, CategoryRepository categoryRepository, VoteRepository voteRepository, OptionRepository optionRepository) {
        this.pollRepository = pollRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.voteRepository = voteRepository;
        this.optionRepository = optionRepository;
    }


    @CachePut(value = "activePolls", key = "#dto.title")
    @Transactional
    public PollCreateDTO createPoll(PollCreateDTO dto) {
        Optional<Polls> existPoll = pollRepository.findByActiveTitle(dto.getTitle());
        if (existPoll.isPresent() && !existPoll.get().isDeleted()) {
            throw new ValidationException("投票已存在");
        }
        Polls poll = new Polls();
        User user = userRepository.findById(dto.getCreator_id())
                .orElseThrow(() -> new ValidationException("用户不存在"));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ValidationException("分类不存在"));
        poll.setTitle(dto.getTitle());
        poll.setDescription(dto.getDescription());
        poll.setCreator(user);
        poll.setStartTime(dto.getStartTime());
        poll.setEndTime(dto.getEndTime());
        poll.setMaxChoice(dto.getMaxChoice());
        poll.setCreatedAt(LocalDateTime.now());
        poll.setCategory(category);
        poll.setAnonymous(dto.getIsAnonymous());
        if (dto.getIsAnonymous()) {
            System.out.println(dto.getAnonymousCode());
            if (dto.getAnonymousCode() == null) {
                throw new ValidationException("匿名投票必须设置匿名码");
            } else if (pollRepository.existsByAnonymousCode(dto.getAnonymousCode())){
                throw new ValidationException("匿名码已存在");
            }
            poll.setAnonymousCode(dto.getAnonymousCode());
        }
        pollRepository.save(poll);
        if (dto.getOptions() == null) {
            throw new ValidationException("投票选项不能为空");
        }
        for (String optionText : dto.getOptions()) {
            Options option = new Options();
            option.setOptionText(optionText);
            option.setPoll(poll);
            optionRepository.save(option);
        }
        return dto;
    }



    @Cacheable(value = "activePolls")
    public List<PollResponseDTO> getActivePolls() {
        return pollRepository.findActivePolls();
    }

    public List<PollResponseDTO> getUserPolls(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("用户不存在"));
        return pollRepository.findPollsByCreator(user);
    }
    public List<PollResponseDTO> getAllPolls(){
        return pollRepository.findAllPolls();
    }


    @Cacheable(value = "categoryPolls", key = "#categoryId")
    public List<PollResponseDTO> getCategoryPolls(int categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ValidationException("分类不存在"));
        List<PollResponseDTO> categoryPolls = pollRepository.findByCategory(category);
        if (categoryPolls.isEmpty()) {
            throw new ValidationException("分类下没有投票");
        }
        return categoryPolls;
    }

//    public PollWithOptionsDTO getPollWithOptions(Long pollId) {
//        List<PollWithOptionsDTO> pollWithOptions = pollRepository.findPollWithOptionsByPollId(pollId);
//        if (pollWithOptions.isEmpty()) {
//            throw new ValidationException("投票不存在");
//        }
//        return pollWithOptions.get(0);
//    }

    @Transactional
    @CacheEvict(value = {"activePolls", "categoryPolls", "pollOptions"}, allEntries = true)
    public PollUpdateDTO updatePolls(Long pollId, PollUpdateDTO dto) {
        Polls polls = pollRepository.findById(pollId)
                .orElseThrow(() -> new ValidationException("投票不存在"));
        if (polls.getTitle().equals(dto.getTitle())) {
            if (pollRepository.existsByTitleAndExcludeId(dto.getTitle(), pollId)){
                throw new ValidationException("投票标题已存在");
            }
        }
        if (voteRepository.existsByPoll(polls)) {
            throw new ValidationException("投票已投票，不能修改");
        }
        polls.setTitle(dto.getTitle());
        polls.setDescription(dto.getDescription());
        polls.setStartTime(dto.getStartTime());
        polls.setEndTime(dto.getEndTime());
        polls.setMaxChoice(dto.getMaxChoice());
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ValidationException("分类不存在"));
        polls.setCategory(category);
        polls.setUpdateAt(LocalDateTime.now());
        pollRepository.save(polls);
        return dto;

    }

    @Transactional
    @CacheEvict(value = {"activePolls", "categoryPolls", "pollOptions"}, allEntries = true)
    public void deletePoll(Long pollId) {
        Polls polls= pollRepository.findById(pollId).orElseThrow(() -> new ValidationException("投票不存在"));
        if (polls.isDeleted()) {
            throw new ValidationException("投票已删除");
        }
        polls.setDeleted(true);
        polls.setUpdateAt(LocalDateTime.now());
        pollRepository.save(polls);
    }

    public Polls getPollsByPollId(Long pollId) {
        return pollRepository.findById(pollId)
                .orElseThrow(() -> new ValidationException("投票不存在"));
    }


    public PollWithOptionWithVoteDTO getDetailedPoll(Long pollId, Long userId) {
        Polls polls = pollRepository.findById(pollId)
                .orElseThrow(() -> new ValidationException("投票不存在"));
        List<VoteCountResponseDTO> voteCountResponseDTO = voteRepository.getVoteCountByPoll(polls);
        UserBasicDTO userBasicDTO = new UserBasicDTO(
                polls.getCreator().getUserId(),
                polls.getCreator().getUsername(),
                polls.getCreator().getNickname()
        );
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("用户不存在"));
        boolean hasVoted = voteRepository.existsByPollAndUser(polls, user);
        CategoryDTO categoryDTO = new CategoryDTO(polls.getCategory().getCategoryId(), polls.getCategory().getName());
        return new PollWithOptionWithVoteDTO(
                polls.getPollId(),
                polls.getTitle(),
                polls.getDescription(),
                polls.getStartTime(),
                polls.getEndTime(),
                polls.getMaxChoice(),
                polls.getCreatedAt(),
                hasVoted,
                polls.isAnonymous(),
                categoryDTO,
                userBasicDTO,
                voteCountResponseDTO
        );
    }

    public PollWithOptionWithVoteDTO getAnonymousPoll(String anonymousCode) {
        Polls polls = pollRepository.findByAnonymousCode(anonymousCode)
                .orElseThrow(() -> new ValidationException("匿名码不存在"));
        List<VoteCountResponseDTO> voteCountResponseDTO = voteRepository.getVoteCountByPoll(polls);
        CategoryDTO categoryDTO = new CategoryDTO(polls.getCategory().getCategoryId(), polls.getCategory().getName());
        return new PollWithOptionWithVoteDTO(
                polls.getPollId(),
                polls.getTitle(),
                polls.getDescription(),
                polls.getStartTime(),
                polls.getEndTime(),
                polls.getMaxChoice(),
                polls.getCreatedAt(),
                false,
                polls.isAnonymous(),
                categoryDTO,
                null,
                voteCountResponseDTO
        );
    }
}