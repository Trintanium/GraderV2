/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.amazonaws.services.s3.AmazonS3
 *  com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
 *  com.amazonaws.services.s3.model.ObjectMetadata
 *  com.amazonaws.services.s3.model.PutObjectRequest
 *  com.example.graderbackend.service.AwsS3Service
 *  com.example.graderbackend.service.impl.AwsS3ServiceImpl
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.stereotype.Service
 *  org.springframework.web.multipart.MultipartFile
 */
package com.example.graderbackend.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.graderbackend.service.AwsS3Service;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AwsS3ServiceImpl
implements AwsS3Service {
    @Value(value="${spring.cloud.aws.s3.bucketNamePdf}")
    private String bucketNamePdf;
    @Value(value="${spring.cloud.aws.s3.bucketNamePng}")
    private String bucketNamePng;
    @Autowired
    private AmazonS3 amazonS3;

    public String savePdfToS3(MultipartFile pdf, String fileName) throws IOException {
        if (pdf.getOriginalFilename() == null || !pdf.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("File must be a PDF");
        }
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/pdf");
        metadata.setContentLength(pdf.getSize());
        try (InputStream is = pdf.getInputStream();){
            String string = this.uploadFileToS3(this.bucketNamePdf, fileName, is, metadata);
            return string;
        }
    }

    public String savePngToS3(MultipartFile png, String fileName) throws IOException {
        if (png.getOriginalFilename() == null || !png.getOriginalFilename().toLowerCase().endsWith(".png")) {
            throw new IllegalArgumentException("File must be a PNG");
        }
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/png");
        metadata.setContentLength(png.getSize());
        try (InputStream is = png.getInputStream()) {
            this.uploadFileToS3(this.bucketNamePng, fileName, is, metadata);
        }
        return fileName;
    }

    public String uploadFileToS3(String bucketName, String fileName, InputStream inputStream, ObjectMetadata metadata) {
        PutObjectRequest request = new PutObjectRequest(bucketName, fileName, inputStream, metadata);
        this.amazonS3.putObject(request);
        return this.amazonS3.getUrl(bucketName, fileName).toString();
    }

    public String generatePresignedUrl(String objectKey, String type, long expirationMillis) {
        String bucketName;
        if (objectKey == null || objectKey.isEmpty()) {
            throw new IllegalArgumentException("Object key cannot be null or empty");
        }
        if (type.equalsIgnoreCase("pdf")) {
            bucketName = this.bucketNamePdf;
        } else if (type.equalsIgnoreCase("png")) {
            bucketName = this.bucketNamePng;
        } else {
            throw new IllegalArgumentException("Type must be pdf or png");
        }
        Date expiration = new Date(System.currentTimeMillis() + expirationMillis);
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, objectKey).withExpiration(expiration);
        return this.amazonS3.generatePresignedUrl(request).toString();
    }

    public void deleteFileFromS3(String objectKey, String type) {
        String bucketName;
        if (objectKey == null || objectKey.isEmpty()) {
            throw new IllegalArgumentException("Object key cannot be null or empty");
        }
        if (type.equalsIgnoreCase("pdf")) {
            bucketName = this.bucketNamePdf;
        } else if (type.equalsIgnoreCase("png")) {
            bucketName = this.bucketNamePng;
        } else {
            throw new IllegalArgumentException("Type must be pdf or png");
        }
        if (this.amazonS3.doesObjectExist(bucketName, objectKey)) {
            this.amazonS3.deleteObject(bucketName, objectKey);
        } else {
            System.out.println("Warning: File not found in S3: " + objectKey);
        }
    }
}

