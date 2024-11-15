package com.fgiotlead.ds.edge.tb.model.service;

import com.fgiotlead.ds.edge.tb.model.entity.TbDeviceEntity;

import java.util.List;

public interface TbDeviceService {
    List<TbDeviceEntity> findAllByType(String type);
}
