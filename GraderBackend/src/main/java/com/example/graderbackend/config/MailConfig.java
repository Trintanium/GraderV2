/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.config.MailConfig
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Configuration
 *  org.springframework.mail.javamail.JavaMailSender
 *  org.springframework.mail.javamail.JavaMailSenderImpl
 */
package com.example.graderbackend.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
    @Value(value="${spring.mail.host}")
    private String host;
    @Value(value="${spring.mail.port}")
    private int port;
    @Value(value="${spring.mail.username}")
    private String username;
    @Value(value="${spring.mail.password}")
    private String password;

    @Bean
    public JavaMailSender getMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.host);
        mailSender.setPort(this.port);
        mailSender.setUsername(this.username);
        mailSender.setPassword(this.password);
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        return mailSender;
    }
}

