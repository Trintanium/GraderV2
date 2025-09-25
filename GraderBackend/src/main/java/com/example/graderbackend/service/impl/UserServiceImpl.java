package com.example.graderbackend.service.impl;

import com.example.graderbackend.dto.entity.UserDto;
import com.example.graderbackend.entity.User;
import com.example.graderbackend.repository.UserRepository;
import com.example.graderbackend.service.AwsS3Service;
import com.example.graderbackend.service.CurrentSessionService;
import com.example.graderbackend.service.ModelMapperService;
import com.example.graderbackend.service.UserService;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CurrentSessionService currentSessionService;
    private final AwsS3Service awsS3Service;
    private final ModelMapperService mapperService;

    private static final long IMG_EXPIRED_ONE_DAYS = 86_400_000L;

    public UserServiceImpl(
            UserRepository userRepository,
            CurrentSessionService currentSessionService,
            AwsS3Service awsS3Service,
            ModelMapperService mapperService
    ) {
        this.userRepository = userRepository;
        this.currentSessionService = currentSessionService;
        this.awsS3Service = awsS3Service;
        this.mapperService = mapperService;
    }

    @Override
    public UserDto getCurrentUser() {
        User user = currentSessionService.getCurrentUser();
        UserDto dto = mapperService.toDto(user, UserDto.class);
        setProfilePicturePresignedUrl(user, dto, IMG_EXPIRED_ONE_DAYS);
        return dto;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> dtos = mapperService.toListDto(users, UserDto.class);

        for (int i = 0; i < users.size(); i++) {
            setProfilePicturePresignedUrl(users.get(i), dtos.get(i), IMG_EXPIRED_ONE_DAYS);
        }

        return dtos;
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserDto dto = mapperService.toDto(user, UserDto.class);
        setProfilePicturePresignedUrl(user, dto, IMG_EXPIRED_ONE_DAYS);
        return dto;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        UserDto dto = mapperService.toDto(user, UserDto.class);
        if (Objects.nonNull(user)) {
            setProfilePicturePresignedUrl(user, dto, IMG_EXPIRED_ONE_DAYS);
        }
        return dto;
    }

    @Override
    public UserDto updateUser(String username, MultipartFile png) throws IOException {
        User user = currentSessionService.getCurrentUser();

        if (username != null && !username.isBlank()) {
            user.setUsername(username);
        }

        if (png != null && !png.isEmpty()) {
            if (Objects.nonNull(user.getProfilePicture()) && !user.getProfilePicture().isEmpty()) {
                awsS3Service.deleteFileFromS3(user.getProfilePicture(), "png");
            }
            String uniqueFileName = "user_" + user.getId() + "_" + System.currentTimeMillis() + ".png";
            String pngKey = awsS3Service.savePngToS3(png, uniqueFileName);
            user.setProfilePicture(pngKey);
        }

        User savedUser = userRepository.save(user);
        UserDto dto = mapperService.toDto(savedUser, UserDto.class);
        setProfilePicturePresignedUrl(savedUser, dto, IMG_EXPIRED_ONE_DAYS);

        return dto;
    }

    @Override
    public void deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        deleteUserProfileIfExists(user);
        userRepository.delete(user);
    }

    @Override
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        deleteUserProfileIfExists(user);
        userRepository.delete(user);
    }

    @Override
    public UserDto uploadUserProfile(MultipartFile png) throws IOException {
        User user = currentSessionService.getCurrentUser();
        String uniqueFileName = "user_" + user.getId() + "_" + System.currentTimeMillis() + ".png";
        String pngKey = awsS3Service.savePngToS3(png, uniqueFileName);
        user.setProfilePicture(pngKey);

        User savedUser = userRepository.save(user);
        UserDto dto = mapperService.toDto(savedUser, UserDto.class);
        dto.setProfilePicture(awsS3Service.generatePresignedUrl(pngKey, "png", 3_600_000L));

        return dto;
    }

    // ----------------- Private Helpers -----------------

    private void setProfilePicturePresignedUrl(User user, UserDto dto, long expiryMillis) {
        if (Objects.nonNull(user.getProfilePicture()) && !user.getProfilePicture().isEmpty()) {
            String presignedUrl = awsS3Service.generatePresignedUrl(user.getProfilePicture(), "png", expiryMillis);
            dto.setProfilePicture(presignedUrl);
        }
    }

    private void deleteUserProfileIfExists(User user) {
        if (Objects.nonNull(user.getProfilePicture()) && !user.getProfilePicture().isEmpty()) {
            awsS3Service.deleteFileFromS3(user.getProfilePicture(), "png");
        }
    }
}