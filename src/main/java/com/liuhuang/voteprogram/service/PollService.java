package com.liuhuang.voteprogram.service;


import com.liuhuang.voteprogram.dto.PollCreateDTO;
import com.liuhuang.voteprogram.exception.ValidationException;
import com.liuhuang.voteprogram.model.Category;
import com.liuhuang.voteprogram.model.Polls;
import com.liuhuang.voteprogram.model.User;
import com.liuhuang.voteprogram.repository.CategoryRepository;
import com.liuhuang.voteprogram.repository.PollRepository;
import com.liuhuang.voteprogram.repository.UserRepository;
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

    public PollService(PollRepository pollRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.pollRepository = pollRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public Polls getPollById(Long id) {
        return pollRepository.findById(id).orElse(null);
    }

    @Transactional
    public Polls createPoll(PollCreateDTO dto) {
        Optional<Polls> existPoll = pollRepository.findByActiveTitle(dto.getTitle());
        if (existPoll.isPresent() && !existPoll.get().isDeleted()) {
            throw new ValidationException("投票已存在");
        }
        return createAndSavePoll(dto);
    }

    private Polls createAndSavePoll(PollCreateDTO dto) {
        Polls poll = new Polls();
        User user = userRepository.findById(dto.getCreator_id())
                .orElseThrow(() -> new ValidationException("用户不存在"));
        Category category = categoryRepository.findById(dto.getCategory_id())
                .orElseThrow(() -> new ValidationException("分类不存在"));
        poll.setTitle(dto.getTitle());
        poll.setDescription(dto.getDescription());
        poll.setCreator(user);
        poll.setStartTime(dto.getStartTime());
        poll.setEndTime(dto.getEndTime());
        poll.setMaxChoice(dto.getMaxChoice());
        poll.setCreatedAt(LocalDateTime.now());
        poll.setCategory(category);

        return pollRepository.save(poll);
    }

    public List<Polls> getActivePolls() {
        return pollRepository.findActivePolls();
    }

}
