/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.dto.meesageQ.SubmissionResultMessage
 *  com.example.graderbackend.service.SubmissionService
 *  com.example.graderbackend.service.impl.SubmissionConsumer
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.amqp.rabbit.annotation.RabbitListener
 *  org.springframework.stereotype.Service
 */
package com.example.graderbackend.service.impl;

import com.example.graderbackend.dto.meesageQ.SubmissionResultMessage;
import com.example.graderbackend.service.SubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SubmissionConsumer {
    private final SubmissionService submissionService;

    public SubmissionConsumer(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @RabbitListener(queues={"${rabbitmq.queue.result}"})
    public void handleResult(SubmissionResultMessage result) {
        if (result.getSubmissionId() == null) {
            log.warn("Received result with null submissionId: {}", result);
            return;
        }
        log.info("Received result for submissionId: {}", result.getSubmissionId());
        this.submissionService.updateSubmissionResult(result);
    }
}

