/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.service.ModelMapperService
 *  com.example.graderbackend.service.impl.ModelMapperServiceImpl
 *  org.modelmapper.ModelMapper
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.example.graderbackend.service.impl;

import com.example.graderbackend.service.ModelMapperService;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModelMapperServiceImpl
implements ModelMapperService {
    @Autowired
    private ModelMapper modelMapper;

    public <D, T> D toDto(T entity, Class<D> dtoClass) {
        return (D)this.modelMapper.map(entity, dtoClass);
    }

    public <D, T> List<D> toListDto(List<T> entityList, Class<D> dtoClass) {
        return entityList.stream().map(e -> this.modelMapper.map(e, dtoClass)).collect(Collectors.toList());
    }
}

