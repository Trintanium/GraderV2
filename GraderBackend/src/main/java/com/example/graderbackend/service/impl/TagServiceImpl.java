package com.example.graderbackend.service.impl;

import com.example.graderbackend.dto.entity.TagDto;
import com.example.graderbackend.entity.Tag;
import com.example.graderbackend.exception.TagNotFoundException;
import com.example.graderbackend.repository.TagRepository;
import com.example.graderbackend.service.ModelMapperService;
import com.example.graderbackend.service.TagService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final ModelMapperService modelMapperService;

    public TagServiceImpl(TagRepository tagRepository, ModelMapperService modelMapperService) {
        this.tagRepository = tagRepository;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public List<TagDto> getAllTag() {
        List<Tag> tags = tagRepository.findAll();
        return modelMapperService.toListDto(tags, TagDto.class);
    }

    @Override
    public TagDto getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException(id));
        return modelMapperService.toDto(tag, TagDto.class);
    }

    @Override
    public TagDto createTag(String name) {
        Tag tag = new Tag();
        tag.setName(name);
        Tag savedTag = tagRepository.save(tag);
        return modelMapperService.toDto(savedTag, TagDto.class);
    }

    @Override
    public TagDto updateTag(Long id, String name) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException(id));
        tag.setName(name);
        Tag savedTag = tagRepository.save(tag);
        return modelMapperService.toDto(savedTag, TagDto.class);
    }

    @Override
    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException(id));
        tagRepository.delete(tag);
    }
}