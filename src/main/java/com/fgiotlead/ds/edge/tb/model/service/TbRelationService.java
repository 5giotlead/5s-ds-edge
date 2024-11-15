package com.fgiotlead.ds.edge.tb.model.service;

import com.fgiotlead.ds.edge.tb.model.entity.TbRelationEntity;

import java.util.List;
import java.util.UUID;

public interface TbRelationService {
    List<TbRelationEntity> findAllById(UUID id);
}
