/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.amazonaws.auth.AWSCredentials
 *  com.amazonaws.auth.AWSCredentialsProvider
 *  com.amazonaws.auth.AWSStaticCredentialsProvider
 *  com.amazonaws.auth.BasicAWSCredentials
 *  com.amazonaws.services.s3.AmazonS3
 *  com.amazonaws.services.s3.AmazonS3ClientBuilder
 *  com.example.graderbackend.config.AwsS3Config
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Configuration
 */
package com.example.graderbackend.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsS3Config {
    @Value(value="${spring.cloud.aws.credentials.access-key}")
    private String awsAccessKeyId;
    @Value(value="${spring.cloud.aws.credentials.secret-key}")
    private String awsSecretAccessKey;
    @Value(value="${spring.cloud.aws.s3.region}")
    private String awsRegion;

    @Bean
    public AmazonS3 getAmazonS3() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(this.awsAccessKeyId, this.awsSecretAccessKey);
        return (AmazonS3)((AmazonS3ClientBuilder)((AmazonS3ClientBuilder)AmazonS3ClientBuilder.standard().withRegion(this.awsRegion)).withCredentials((AWSCredentialsProvider)new AWSStaticCredentialsProvider((AWSCredentials)awsCredentials))).build();
    }
}

