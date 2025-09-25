/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.controller.TagController
 *  com.example.graderbackend.dto.entity.TagDto
 *  com.example.graderbackend.service.TagService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.example.graderbackend.controller;

import com.example.graderbackend.dto.entity.TagDto;
import com.example.graderbackend.service.TagService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/tag"})
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping
    public ResponseEntity<List<TagDto>> getAllTag() {
        List tags = this.tagService.getAllTag();
        return ResponseEntity.ok(tags);
    }

    @GetMapping(value={"/{id}"})
    public ResponseEntity<TagDto> getTagById(@PathVariable Long id) {
        TagDto tag = this.tagService.getTagById(id);
        return ResponseEntity.ok(tag);
    }

    @PostMapping
    public ResponseEntity<TagDto> createTag(@RequestBody TagDto tagDto) {
        TagDto tag = this.tagService.createTag(tagDto.getName());
        return ResponseEntity.ok(tag);
    }

    @PutMapping(value={"/{id}"})
    public ResponseEntity<TagDto> updateTag(@PathVariable Long id, @RequestBody TagDto tagDto) {
        TagDto tag = this.tagService.updateTag(id, tagDto.getName());
        return ResponseEntity.ok(tag);
    }

    @DeleteMapping(value={"/{id}"})
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        this.tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}

