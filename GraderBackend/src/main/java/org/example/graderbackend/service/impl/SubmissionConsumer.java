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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class SubmissionConsumer {
    private static final Logger logger = LoggerFactory.getLogger(SubmissionConsumer.class);
    private final SubmissionService submissionService;

    public SubmissionConsumer(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @RabbitListener(queues={"${rabbitmq.queue.result}"})
    public void handleResult(SubmissionResultMessage result) {
        if (result.getSubmissionId() == null) {
            logger.warn("Received result with null submissionId: {}", (Object)result);
            return;
        }
        logger.info("Received result for submissionId: {}", (Object)result.getSubmissionId());
        this.submissionService.updateSubmissionResult(result);
    }
}

