package com.example.graderbackend.service.impl;

import com.example.graderbackend.dto.entity.SubmissionDto;
import com.example.graderbackend.dto.entity.TestCaseDto;
import com.example.graderbackend.dto.meesageQ.SubmissionResultMessage;
import com.example.graderbackend.dto.meesageQ.SubmissionSendMessage;
import com.example.graderbackend.entity.Problem;
import com.example.graderbackend.entity.Status;
import com.example.graderbackend.entity.Submission;
import com.example.graderbackend.entity.User;
import com.example.graderbackend.repository.SubmissionRepository;
import com.example.graderbackend.repository.TestCaseRepository;
import com.example.graderbackend.service.CurrentSessionService;
import com.example.graderbackend.service.ModelMapperService;
import com.example.graderbackend.service.SubmissionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final TestCaseRepository testCaseRepository;
    private final SubmissionProducer submissionProducer;
    private final ModelMapperService modelMapperService;
    private final CurrentSessionService currentSessionService;

    public SubmissionServiceImpl(
            SubmissionRepository submissionRepository,
            TestCaseRepository testCaseRepository,
            SubmissionProducer submissionProducer,
            ModelMapperService modelMapperService,
            CurrentSessionService currentSessionService
    ) {
        this.submissionRepository = submissionRepository;
        this.testCaseRepository = testCaseRepository;
        this.submissionProducer = submissionProducer;
        this.modelMapperService = modelMapperService;
        this.currentSessionService = currentSessionService;
    }

    @Transactional
    public SubmissionDto createSubmission(SubmissionDto dto) {
        User user = currentSessionService.getCurrentUser();
        dto.setUserId(user.getId());

        Submission submission = buildSubmission(dto);
        Submission saved = submissionRepository.save(submission);

        sendToWorker(saved);

        return modelMapperService.toDto(saved, SubmissionDto.class);
    }

    public SubmissionDto getSubmissionById(Long id) {
        return submissionRepository.findById(id)
                .map(sub -> modelMapperService.toDto(sub, SubmissionDto.class))
                .orElseThrow(() -> new RuntimeException("Submission not found with id " + id));
    }

    public List<SubmissionDto> getAllSubmissions() {
        return modelMapperService.toListDto(submissionRepository.findAll(), SubmissionDto.class);
    }

    @Transactional
    public SubmissionDto updateSubmission(Long id, SubmissionDto dto) {
        Submission updated = submissionRepository.findById(id).map(submission -> {
            submission.setCode(dto.getCode());
            submission.setLanguage(dto.getLanguage());
            submission.setScore(dto.getScore());
            submission.setStatus(dto.getStatus());
            return submissionRepository.save(submission);
        }).orElseThrow(() -> new RuntimeException("Submission not found with id " + id));

        return modelMapperService.toDto(updated, SubmissionDto.class);
    }

    @Transactional
    public void deleteSubmission(Long id) {
        if (!submissionRepository.existsById(id)) {
            throw new RuntimeException("Submission not found with id " + id);
        }
        submissionRepository.deleteById(id);
    }

    @Transactional
    public void updateSubmissionResult(SubmissionResultMessage result) {
        Submission submission = submissionRepository.findById(result.getSubmissionId())
                .orElseThrow(() -> new RuntimeException("Submission not found with id " + result.getSubmissionId()));

        submission.setScore(result.getScore());
        submission.setStatus(result.getStatus().equalsIgnoreCase("Accepted") ? Status.ACCEPTED : Status.FAILED);

        submissionRepository.save(submission);
    }

    public SubmissionDto getMyLatestSubmission() {
        User user = currentSessionService.getCurrentUser();
        Submission submission = submissionRepository.findTopByUser_IdOrderBySubmittedAtDesc(user.getId())
                .orElseThrow(() -> new RuntimeException("No submissions found for user " + user.getId()));

        return modelMapperService.toDto(submission, SubmissionDto.class);
    }

    public List<SubmissionDto> getMySubmissions() {
        User user = currentSessionService.getCurrentUser();
        List<Submission> submissions = submissionRepository.findByUser_Id(user.getId());
        return modelMapperService.toListDto(submissions, SubmissionDto.class);
    }

    private Submission buildSubmission(SubmissionDto dto) {
        Submission submission = new Submission();
        submission.setUser(buildUser(dto.getUserId()));
        submission.setProblem(buildProblem(dto.getProblemId()));
        submission.setCode(dto.getCode());
        submission.setLanguage(dto.getLanguage());
        submission.setScore(Objects.requireNonNullElse(dto.getScore(), 0.0f));
        submission.setStatus(Objects.requireNonNullElse(dto.getStatus(), Status.PENDING));
        submission.setSubmittedAt(LocalDateTime.now());
        return submission;
    }

    private User buildUser(Long userId) {
        User user = new User();
        user.setId(userId);
        return user;
    }

    private Problem buildProblem(Long problemId) {
        Problem problem = new Problem();
        problem.setId(problemId);
        return problem;
    }

    private void sendToWorker(Submission saved) {
        List<?> testCases = testCaseRepository.findByProblem_Id(saved.getProblem().getId());
        List<TestCaseDto> testCaseDtos = modelMapperService.toListDto(testCases, TestCaseDto.class);
        SubmissionDto submissionDto = modelMapperService.toDto(saved, SubmissionDto.class);

        SubmissionSendMessage message = new SubmissionSendMessage();
        message.setSubmissionId(saved.getId());
        message.setSubmissionDto(submissionDto);
        message.setTestCasesDtoList(testCaseDtos);

        submissionProducer.sendSubmission(message);
    }
}