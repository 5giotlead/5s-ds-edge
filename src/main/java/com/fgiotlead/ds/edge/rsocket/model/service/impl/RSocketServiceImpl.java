package com.fgiotlead.ds.edge.rsocket.model.service.impl;

import com.fgiotlead.ds.edge.event.SignageEventPublisher;
import com.fgiotlead.ds.edge.event.entity.StatusEvent;
import com.fgiotlead.ds.edge.rsocket.model.service.RSocketService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.nio.ByteBuffer;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class RSocketServiceImpl implements RSocketService {

    private RSocketRequester requester;
    private SignageEventPublisher eventPublisher;
    private ApplicationEventPublisher aep;

    @Override
    public void register() {
        requester
                .route("signage.register")
                .send()
                .subscribe();
    }

    @Override
    public Flux<ByteBuffer> fileDownload(UUID fileId) {
        return requester
                .route("signage.file." + fileId + ".download")
                .retrieveFlux(ByteBuffer.class);
    }

    @EventListener(condition = "#statusEvent.status.name != 'PENDING'")
    public void responseStatus(StatusEvent statusEvent) {
        requester
                .route("signage.status")
                .data(statusEvent.getStatus())
                .send()
                .subscribe();
    }

    private void settingsCheck() {
        requester
                .route("signage.settings.check")
                .send()
                .subscribe();
    }

    @Scheduled(fixedDelay = 60000L)
    private void heartbeat() {
        requester
                .route("signage.heartbeat")
                .send()
                .subscribe();
    }

}
