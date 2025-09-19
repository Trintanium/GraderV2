package com.example.graderbackend.service.impl;

import com.example.graderbackend.dto.entity.ProblemDto;
import com.example.graderbackend.dto.entity.TagDto;
import com.example.graderbackend.entity.Difficulty;
import com.example.graderbackend.entity.Problem;
import com.example.graderbackend.entity.ProblemTag;
import com.example.graderbackend.entity.Tag;
import com.example.graderbackend.repository.ProblemRepository;
import com.example.graderbackend.service.AwsS3Service;
import com.example.graderbackend.service.ModelMapperService;
import com.example.graderbackend.service.ProblemService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProblemServiceImpl implements ProblemService {

    private static final long CACHE_TTL_SECONDS = 86_400L;
    private static final long IMG_EXPIRED_ONE_DAYS = 86_400_000L;

    private final ProblemRepository problemRepository;
    private final AwsS3Service awsS3Service;
    private final ModelMapperService mapperService;
    private final RedisTemplate<String, Object> redisTemplate;

    public ProblemServiceImpl(
            ProblemRepository problemRepository,
            AwsS3Service awsS3Service,
            ModelMapperService mapperService,
            RedisTemplate<String, Object> redisTemplate
    ) {
        this.problemRepository = problemRepository;
        this.awsS3Service = awsS3Service;
        this.mapperService = mapperService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<ProblemDto> getAllProblem() {
        List<Problem> problems = problemRepository.findAll();
        List<ProblemDto> problemDtos = mapperService.toListDto(problems, ProblemDto.class);
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();

        for (int i = 0; i < problems.size(); i++) {
            Problem problem = problems.get(i);
            ProblemDto dto = problemDtos.get(i);

            String cacheKey = "problemDto:" + problem.getId();
            ProblemDto cachedDto = (ProblemDto) ops.get(cacheKey);

            if (cachedDto != null) {
                dto.setTitle(cachedDto.getTitle());
                dto.setDifficulty(cachedDto.getDifficulty());
                dto.setPdfUrl(cachedDto.getPdfUrl());
                continue;
            }

            setPdfPresignedUrl(problem, dto);
            cacheProblemDto(dto);
        }

        return problemDtos;
    }

    @Override
    public ProblemDto getProblemById(Long id) {
        String cacheKey = "problemDto:" + id;
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ProblemDto cachedDto = (ProblemDto) ops.get(cacheKey);

        if (cachedDto != null) {
            setPdfPresignedUrlFromDto(cachedDto);
            return cachedDto;
        }

        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problem not found"));
        ProblemDto dto = mapperService.toDto(problem, ProblemDto.class);
        setPdfPresignedUrl(problem, dto);
        cacheProblemDto(dto);
        return dto;
    }

    @Override
    public List<TagDto> getTagsByProblemId(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("Problem not found"));

        return problem.getProblemTags().stream()
                .map(pt -> new TagDto(pt.getTag().getId(), pt.getTag().getName()))
                .toList();
    }

    @Override
    public ProblemDto createProblemWithPdf(String title, Difficulty difficulty, MultipartFile pdf) throws IOException {
        validatePdfFile(pdf);

        Problem problem = new Problem();
        problem.setTitle(title);
        problem.setDifficulty(difficulty);

        String uniqueFileName = "problem_" + System.currentTimeMillis() + "_" + pdf.getOriginalFilename();
        awsS3Service.savePdfToS3(pdf, uniqueFileName);
        problem.setPdf(uniqueFileName);

        Problem saved = problemRepository.save(problem);
        ProblemDto dto = mapperService.toDto(saved, ProblemDto.class);
        setPdfPresignedUrl(saved, dto);
        cacheProblemDto(dto);

        return dto;
    }

    @Override
    public ProblemDto updateProblem(Long id, String title, String difficulty, MultipartFile pdf, List<Long> tagIds) throws IOException {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problem not found"));

        problem.setTitle(title);
        problem.setDifficulty(Difficulty.valueOf(difficulty));

        if (pdf != null) {
            validatePdfFile(pdf);
            deletePdfIfExists(problem);
            String uniqueFileName = "problem_" + System.currentTimeMillis() + "_" + pdf.getOriginalFilename();
            awsS3Service.savePdfToS3(pdf, uniqueFileName);
            problem.setPdf(uniqueFileName);
        }

        if (tagIds != null) {
            problem.getProblemTags().clear();
            List<ProblemTag> newProblemTags = tagIds.stream()
                    .map(tagId -> {
                        ProblemTag pt = new ProblemTag();
                        pt.setProblem(problem);
                        Tag tag = new Tag();
                        tag.setId(tagId);
                        pt.setTag(tag);
                        return pt;
                    }).toList();
            problem.setProblemTags(newProblemTags);
        }

        problem.setUpdatedAt(LocalDateTime.now());
        Problem saved = problemRepository.save(problem);

        ProblemDto dto = mapperService.toDto(saved, ProblemDto.class);
        setPdfPresignedUrl(saved, dto);
        cacheProblemDto(dto);

        return dto;
    }

    @Override
    public void deleteProblem(Long id) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problem not found"));

        deletePdfIfExists(problem);
        problemRepository.delete(problem);
        redisTemplate.delete("problemDto:" + id);
    }

    // -------------------- Private Helpers --------------------

    private void setPdfPresignedUrl(Problem problem, ProblemDto dto) {
        if (problem.getPdf() != null && !problem.getPdf().isEmpty()) {
            String presignedUrl = awsS3Service.generatePresignedUrl(problem.getPdf(), "pdf", IMG_EXPIRED_ONE_DAYS);
            dto.setPdfUrl(presignedUrl);
        }
    }

    private void setPdfPresignedUrlFromDto(ProblemDto dto) {
        if (dto.getPdf() != null && !dto.getPdf().isEmpty()) {
            dto.setPdfUrl(awsS3Service.generatePresignedUrl(dto.getPdf(), "pdf", IMG_EXPIRED_ONE_DAYS));
        }
    }

    private void cacheProblemDto(ProblemDto dto) {
        String cacheKey = "problemDto:" + dto.getId();
        redisTemplate.opsForValue().set(cacheKey, dto, CACHE_TTL_SECONDS, TimeUnit.SECONDS);
    }

    private void validatePdfFile(MultipartFile pdf) {
        if (pdf.getOriginalFilename() == null || !pdf.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("File must be PDF");
        }
    }

    private void deletePdfIfExists(Problem problem) {
        if (problem.getPdf() != null && !problem.getPdf().isEmpty()) {
            try {
                awsS3Service.deleteFileFromS3(problem.getPdf(), "pdf");
            } catch (RuntimeException e) {
                System.out.println("Warning: could not delete old PDF, skipping: " + problem.getPdf());
            }
        }
    }
}