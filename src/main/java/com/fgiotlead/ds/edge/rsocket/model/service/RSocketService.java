package com.fgiotlead.ds.edge.rsocket.model.service;

import com.fgiotlead.ds.edge.event.entity.StatusEvent;
import reactor.core.publisher.Flux;

import java.nio.ByteBuffer;
import java.util.UUID;

public interface RSocketService {
    void register();

    void responseStatus(StatusEvent statusEvent);

    Flux<ByteBuffer> fileDownload(UUID fileId);
}
