package com.fgiotlead.ds.edge.model.service;

import com.fgiotlead.ds.edge.model.entity.SignageStyleEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface SignageStyleService {
    List<SignageStyleEntity> findAll();

    Optional<SignageStyleEntity> findById(UUID id);

    Optional<SignageStyleEntity> findByName(String name);

    void saveAll(Set<SignageStyleEntity> styles);

    SignageStyleEntity save(SignageStyleEntity style);

    void delete(SignageStyleEntity style);

    void checkUselessStyles();
}
