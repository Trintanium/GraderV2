/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.dto.entity.TagDto
 *  com.example.graderbackend.service.TagService
 */
package com.example.graderbackend.service;

import com.example.graderbackend.dto.entity.TagDto;
import java.util.List;

public interface TagService {
    public List<TagDto> getAllTag();

    public TagDto getTagById(Long var1);

    public TagDto createTag(String var1);

    public TagDto updateTag(Long var1, String var2);

    public void deleteTag(Long var1);
}

