package com.fgiotlead.ds.edge.tb.model.service;

import com.fgiotlead.ds.edge.tb.model.entity.TbDeviceCredentialsEntity;

import java.util.Optional;
import java.util.UUID;

public interface TbDeviceCredentialsService {
    Optional<TbDeviceCredentialsEntity> findByDeviceId(UUID deviceId);
}
