/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.amazonaws.services.s3.model.ObjectMetadata
 *  com.example.graderbackend.service.AwsS3Service
 *  org.springframework.web.multipart.MultipartFile
 */
package com.example.graderbackend.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

public interface AwsS3Service {
    public String savePdfToS3(MultipartFile var1, String var2) throws IOException;

    public String savePngToS3(MultipartFile var1, String var2) throws IOException;

    public String uploadFileToS3(String var1, String var2, InputStream var3, ObjectMetadata var4);

    public void deleteFileFromS3(String var1, String var2);

    public String generatePresignedUrl(String var1, String var2, long var3);
}

