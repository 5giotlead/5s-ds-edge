package com.fgiotlead.ds.edge.model.service;

import com.fgiotlead.ds.edge.model.entity.SignageEdgeEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SignageEdgeService {
    List<SignageEdgeEntity> findAll();

    Optional<SignageEdgeEntity> findById(UUID id);

    void downloadSettings(SignageEdgeEntity edge);
}
