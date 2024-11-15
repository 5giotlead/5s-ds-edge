package com.fgiotlead.ds.edge.model.service;

import com.fgiotlead.ds.edge.model.entity.SignageFileEntity;
import com.fgiotlead.ds.edge.model.enumEntity.DownlinkStatus;
import reactor.core.publisher.Flux;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface SignageFileService {

    void save(SignageFileEntity file);

    void deleteById(UUID fileId);

    List<SignageFileEntity> findAll();

    List<SignageFileEntity> findByStatus(DownlinkStatus status);

    Flux<ByteBuffer> checkFiles(Set<SignageFileEntity> files);

    void deleteByBlocksIsNull();

    void checkUselessFiles();
}
