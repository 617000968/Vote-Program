package com.liuhuang.voteprogram.service;


import com.liuhuang.voteprogram.dto.*;
import com.liuhuang.voteprogram.exception.ValidationException;
import com.liuhuang.voteprogram.model.Category;
import com.liuhuang.voteprogram.model.Polls;
import com.liuhuang.voteprogram.model.User;
import com.liuhuang.voteprogram.repository.CategoryRepository;
import com.liuhuang.voteprogram.repository.PollRepository;
import com.liuhuang.voteprogram.repository.UserRepository;
import com.liuhuang.voteprogram.repository.VoteRepository;
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

    public PollService(PollRepository pollRepository, UserRepository userRepository, CategoryRepository categoryRepository, VoteRepository voteRepository) {
        this.pollRepository = pollRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.voteRepository = voteRepository;
    }


    @Transactional
    public PollCreateAndUpdateDTO createPoll(PollCreateAndUpdateDTO dto) {
        Optional<Polls> existPoll = pollRepository.findByActiveTitle(dto.getTitle());
        if (existPoll.isPresent() && !existPoll.get().isDeleted()) {
            throw new ValidationException("投票已存在");
        }
        SavePoll(dto);
        return dto;
    }



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
    public PollCreateAndUpdateDTO updatePolls(Long pollId, PollCreateAndUpdateDTO dto/*, String name **/) {
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
        polls.setAnonymous(dto.getIsAnonymous());
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ValidationException("分类不存在"));
        polls.setCategory(category);
        polls.setUpdateAt(LocalDateTime.now());
        pollRepository.save(polls);
        return dto;

    }



    private void SavePoll(PollCreateAndUpdateDTO dto) {
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
        pollRepository.save(poll);
    }

    @Transactional
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


    public PollWithOptionWithVoteDTO getDetailedPoll(Long pollId) {
        Polls polls = pollRepository.findById(pollId)
                .orElseThrow(() -> new ValidationException("投票不存在"));
        List<VoteCountResponseDTO> voteCountResponseDTO = voteRepository.getVoteCountByPoll(polls);
        UserBasicDTO userBasicDTO = new UserBasicDTO(
                polls.getCreator().getUserId(),
                polls.getCreator().getUsername(),
                polls.getCreator().getNickname()
        );
        CategoryDTO categoryDTO = new CategoryDTO(polls.getCategory().getCategoryId(), polls.getCategory().getName());
        return new PollWithOptionWithVoteDTO(
                polls.getPollId(),
                polls.getTitle(),
                polls.getDescription(),
                polls.getStartTime(),
                polls.getEndTime(),
                polls.getMaxChoice(),
                polls.getCreatedAt(),
                categoryDTO,
                userBasicDTO,
                voteCountResponseDTO
        );
    }
}