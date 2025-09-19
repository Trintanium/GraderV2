/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.service.ModelMapperService
 */
package com.example.graderbackend.service;

import java.util.List;

public interface ModelMapperService {
    public <D, T> D toDto(T var1, Class<D> var2);

    public <D, T> List<D> toListDto(List<T> var1, Class<D> var2);
}

