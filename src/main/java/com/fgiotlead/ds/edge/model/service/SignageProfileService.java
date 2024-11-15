package com.fgiotlead.ds.edge.model.service;

import com.fgiotlead.ds.edge.model.entity.SignageProfileEntity;
import com.fgiotlead.ds.edge.model.enumEntity.SignageType;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface SignageProfileService {
    List<SignageProfileEntity> findAll();

    Optional<SignageProfileEntity> findById(UUID id);

    void saveAll(Map<SignageType, SignageProfileEntity> profiles);

    SignageProfileEntity save(SignageProfileEntity signageProfile);

    Map<String, String> delete(SignageProfileEntity signageProfile);

}