package com.example.graderbackend.controller;

import com.example.graderbackend.dto.entity.TagDto;
import com.example.graderbackend.service.TagService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    // ------------------- GET ALL TAGS -------------------
    @GetMapping
    public ResponseEntity<List<TagDto>> getAllTags() {
        List<TagDto> tags = tagService.getAllTag();
        return ResponseEntity.ok(tags); // 200 OK
    }

    // ------------------- GET TAG BY ID -------------------
    @GetMapping("/{id}")
    public ResponseEntity<TagDto> getTagById(@PathVariable Long id) {
        TagDto tag = tagService.getTagById(id);
        return ResponseEntity.ok(tag); // 200 OK
    }

    // ------------------- CREATE TAG -------------------
    @PostMapping
    public ResponseEntity<TagDto> createTag(@RequestBody TagDto tagDto) {
        TagDto tag = tagService.createTag(tagDto.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(tag); // 201 Created
    }

    // ------------------- UPDATE TAG -------------------
    @PutMapping("/{id}")
    public ResponseEntity<TagDto> updateTag(@PathVariable Long id, @RequestBody TagDto tagDto) {
        TagDto tag = tagService.updateTag(id, tagDto.getName());
        return ResponseEntity.ok(tag); // 200 OK
    }

    // ------------------- DELETE TAG -------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}