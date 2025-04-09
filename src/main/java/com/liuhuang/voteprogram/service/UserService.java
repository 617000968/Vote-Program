package com.liuhuang.voteprogram.service;

import com.liuhuang.voteprogram.dto.UserLoginDTO;
import com.liuhuang.voteprogram.dto.UserRegisterDTO;
import com.liuhuang.voteprogram.dto.UserResponseDTO;
import com.liuhuang.voteprogram.exception.ValidationException;
import com.liuhuang.voteprogram.model.User;
import com.liuhuang.voteprogram.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Min;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public User login(UserLoginDTO dto) {
        User user = userRepository.findByUsername(dto.getUsername()).orElseThrow(() -> new ValidationException("用户名或密码错误"));
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new ValidationException("用户名或密码错误");
        }
        return user;
    }

    public User register(UserRegisterDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new ValidationException("用户名已存在");
        }
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ValidationException("邮箱已存在");
        }
        if (isValidUsername(dto.getUsername())){
            throw new ValidationException("用户名格式不正确,要求账号长度为4到16位，只允许字母、数字、下划线和短横线");
        }
        if (isValidPassword(dto.getPassword())) {
            throw new ValidationException("密码格式不正确,要求密码长度为8到16位，必须包含字母和数字");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setNickname(dto.getNickname());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        return userRepository.save(user);
    }

    public User update(@Min(1) Long id, UserRegisterDTO dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new ValidationException("用户不存在"));
        if (isValidPassword(dto.getPassword())) {
            throw new ValidationException("密码格式不正确,要求密码长度为8到16位，必须包含字母和数字");
        }
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setNickname(dto.getNickname());
        user.setEmail(dto.getEmail());
        return userRepository.save(user);
    }


    public User delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ValidationException("用户不存在"));
        user.setDeleted(true);
        return userRepository.save(user);
    }

    public UserResponseDTO toDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setNickname(user.getNickname());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setDeleted(user.isDeleted());
        return dto;
    }


    // 用户名格式验证
    private boolean isValidUsername(String username) {
        return !username.matches("^[a-zA-Z0-9_-]{4,16}$");
    }

    // 密码格式验证
    private boolean isValidPassword(String password) {
        return !password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{6,18}$");
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("用户不存在"));
    }
}
