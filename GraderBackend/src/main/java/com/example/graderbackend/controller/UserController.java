/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.controller.UserController
 *  com.example.graderbackend.dto.entity.UserDto
 *  com.example.graderbackend.service.UserService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 *  org.springframework.web.multipart.MultipartFile
 */
package com.example.graderbackend.controller;

import com.example.graderbackend.dto.entity.UserDto;
import com.example.graderbackend.service.UserService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value={"/user"})
public class UserController {
    @Autowired
    private UserService userService;

    @PutMapping(value={"/update"})
    public ResponseEntity<UserDto> updateUser(@RequestParam(required=false) String username, @RequestParam(required=false) MultipartFile png) throws IOException {
        UserDto updatedUser = this.userService.updateUser(username, png);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping(value={"/me"})
    public ResponseEntity<UserDto> getCurrentUser() {
        return ResponseEntity.ok(this.userService.getCurrentUser());
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(this.userService.getAllUsers());
    }
}

