package com.example.graderbackend.controller;

import com.example.graderbackend.dto.entity.UserDto;
import com.example.graderbackend.service.UserService;
import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ------------------- UPDATE CURRENT USER -------------------
    @PutMapping("/update")
    public ResponseEntity<UserDto> updateUser(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) MultipartFile png
    ) throws IOException {
        UserDto updatedUser = userService.updateUser(username, png);
        return ResponseEntity.ok(updatedUser); // 200 OK
    }

    // ------------------- GET CURRENT USER -------------------
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        UserDto currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(currentUser); // 200 OK
    }

    // ------------------- GET ALL USERS -------------------
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users); // 200 OK
    }
}