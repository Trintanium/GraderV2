package com.example.graderbackend.service.impl;

import com.example.graderbackend.dto.entity.ProblemTagDto;
import com.example.graderbackend.entity.Problem;
import com.example.graderbackend.entity.ProblemTag;
import com.example.graderbackend.entity.Tag;
import com.example.graderbackend.repository.ProblemRepository;
import com.example.graderbackend.repository.ProblemTagRepository;
import com.example.graderbackend.repository.TagRepository;
import com.example.graderbackend.service.ModelMapperService;
import com.example.graderbackend.service.ProblemTagService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProblemTagServiceImpl implements ProblemTagService {

    private final ProblemTagRepository problemTagRepository;
    private final ProblemRepository problemRepository;
    private final TagRepository tagRepository;
    private final ModelMapperService modelMapperService;

    public ProblemTagServiceImpl(
            ProblemTagRepository problemTagRepository,
            ProblemRepository problemRepository,
            TagRepository tagRepository,
            ModelMapperService modelMapperService
    ) {
        this.problemTagRepository = problemTagRepository;
        this.problemRepository = problemRepository;
        this.tagRepository = tagRepository;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public List<ProblemTagDto> getTagsByProblem(Long problemId) {
        List<ProblemTag> problemTags = (problemId == null)
                ? problemTagRepository.findAll()
                : problemTagRepository.findByProblemId(problemId);
        return modelMapperService.toListDto(problemTags, ProblemTagDto.class);
    }

    @Override
    @Transactional
    public ProblemTagDto createProblemTag(Long problemId, Long tagId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("Problem not found"));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        ProblemTag problemTag = new ProblemTag();
        problemTag.setProblem(problem);
        problemTag.setTag(tag);

        ProblemTag saved = problemTagRepository.save(problemTag);
        return modelMapperService.toDto(saved, ProblemTagDto.class);
    }

    @Override
    @Transactional
    public void updateProblemTags(Long problemId, List<Long> tagIds) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("Problem not found"));

        List<ProblemTag> oldTags = problemTagRepository.findByProblemId(problemId);
        problemTagRepository.deleteAll(oldTags);

        List<ProblemTag> newTags = tagIds.stream()
                .map(tagId -> {
                    Tag tag = tagRepository.findById(tagId)
                            .orElseThrow(() -> new RuntimeException("Tag not found"));
                    ProblemTag pt = new ProblemTag();
                    pt.setProblem(problem);
                    pt.setTag(tag);
                    return pt;
                })
                .collect(Collectors.toList());

        problemTagRepository.saveAll(newTags);
    }

    @Override
    public void deleteProblemTag(Long id) {
        problemTagRepository.deleteById(id);
    }
}