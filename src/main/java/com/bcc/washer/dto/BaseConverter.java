package com.bcc.washer.dto;

import java.util.List;

public abstract class BaseConverter<T, U> {

    abstract T convertDtoToModel(U dto);
    abstract U convertModelToDto(T entity);

    List<U> convertModelListToDtoList(List<T> models) {
        return models.stream().map(this::convertModelToDto).toList();
    }

    List<T> convertDtoListToModelList(List<U> dtos) {
        return dtos.stream().map(this::convertDtoToModel).toList();
    }

}
