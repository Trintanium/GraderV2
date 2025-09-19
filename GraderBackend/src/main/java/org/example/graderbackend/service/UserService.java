/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.dto.entity.UserDto
 *  com.example.graderbackend.service.UserService
 *  org.springframework.stereotype.Service
 *  org.springframework.web.multipart.MultipartFile
 */
package com.example.graderbackend.service;

import com.example.graderbackend.dto.entity.UserDto;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UserService {
    public UserDto getCurrentUser();

    public List<UserDto> getAllUsers();

    public UserDto getUserById(Long var1);

    public UserDto getUserByEmail(String var1);

    public UserDto updateUser(String var1, MultipartFile var2) throws IOException;

    public void deleteUserByEmail(String var1);

    public void deleteUserById(Long var1);

    public UserDto uploadUserProfile(MultipartFile var1) throws IOException;
}

