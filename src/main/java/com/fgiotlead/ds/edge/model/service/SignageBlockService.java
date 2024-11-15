package com.fgiotlead.ds.edge.model.service;

import com.fgiotlead.ds.edge.model.entity.SignageBlockEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SignageBlockService {
    List<SignageBlockEntity> findAll();

    Optional<SignageBlockEntity> findById(UUID id);

    void save(SignageBlockEntity signageBlock);

    void delete(SignageBlockEntity signageBlock);

    void persist(SignageBlockEntity signageBlock);

    void update(SignageBlockEntity signageBlock);

    void remove(SignageBlockEntity signageBlock);
}
