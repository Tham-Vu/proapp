package com.example.user_management.model.mapper;

import java.util.List;

public interface EntityMapper <D, E>{
    E toEntity(D dto);
    D toDto(E entity);
    List<D> toDto(List<E> entityList);
    List<E> toEntity(List<D> dtolist);
}
