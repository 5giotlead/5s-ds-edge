package com.fgiotlead.ds.edge.model.service.Impl;


import com.fgiotlead.ds.edge.event.SignageEventPublisher;
import com.fgiotlead.ds.edge.event.entity.SignageEdgeEvent;
import com.fgiotlead.ds.edge.model.entity.SignageEdgeEntity;
import com.fgiotlead.ds.edge.model.entity.SignageFileEntity;
import com.fgiotlead.ds.edge.model.entity.SignageStyleEntity;
import com.fgiotlead.ds.edge.model.enumEntity.DownlinkStatus;
import com.fgiotlead.ds.edge.model.repository.SignageEdgeRepository;
import com.fgiotlead.ds.edge.model.service.SignageEdgeService;
import com.fgiotlead.ds.edge.model.service.SignageFileService;
import com.fgiotlead.ds.edge.model.service.SignageStyleService;
import com.fgiotlead.ds.edge.quartz.model.service.ScheduleControlService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@AllArgsConstructor
public class SignageEdgeServiceImpl implements SignageEdgeService {

    private SignageEdgeRepository edgeRepository;
    private SignageStyleService styleService;
    private SignageFileService fileService;
    private ScheduleControlService scheduleControlService;
    private SignageEventPublisher eventPublisher;

    @Override
    public List<SignageEdgeEntity> findAll() {
        return edgeRepository.findAll();
    }

    @Override
    public Optional<SignageEdgeEntity> findById(UUID id) {
        return edgeRepository.findById(id);
    }

    @Override
    public void downloadSettings(SignageEdgeEntity edge) {
        Set<SignageStyleEntity> styles = new HashSet<>();
        Set<SignageFileEntity> files = new HashSet<>();
        edge.getProfiles().forEach(
                profile -> profile.getSchedules().forEach(
                        schedule -> {
                            styles.add(schedule.getStyle());
                            schedule.getStyle().getBlocks().forEach(
                                    block -> files.addAll(block.getFiles())
                            );
                        }
                )
        );
        Flux<ByteBuffer> flux = fileService.checkFiles(files);
        if (!flux.equals(Flux.empty())) {
            flux
                    .doOnComplete(() -> downloadComplete(edge))
                    .doOnError(error -> downloadError(error, edge))
                    .doFinally(signalType -> {
                        styleService.saveAll(styles);
                        this.eventPublisher.publishEdgeEvent(this, edge);
                        log.trace("Type: {}, Status: {}", signalType, edge.getStatus());
                    })
                    .subscribe();
        } else {
            styleService.saveAll(styles);
            edge.setStatus(DownlinkStatus.DEPLOYED);
            this.deploy(edge);
        }
    }

    @EventListener(condition = "#signageEdgeEvent.edge.status.name == 'DEPLOYED'")
    public void saveAfterSuccess(SignageEdgeEvent signageEdgeEvent) {
        SignageEdgeEntity edge = signageEdgeEvent.getEdge();
        this.deploy(edge);
    }

    @EventListener(condition = "#signageEdgeEvent.edge.status.name == 'ERROR'")
    public void saveAfterError(SignageEdgeEvent signageEdgeEvent) {
        SignageEdgeEntity edge = signageEdgeEvent.getEdge();
        edgeRepository.save(edge);
        this.eventPublisher.publishStatusEvent(this, edge.getStatus());
    }

    private void downloadComplete(SignageEdgeEntity edge) {
        AtomicBoolean hasError = new AtomicBoolean(false);
        edge.getProfiles().forEach(
                profile -> profile.getSchedules().forEach(
                        schedule -> schedule.getStyle().getBlocks().forEach(
                                block -> hasError.set(block.getFiles().stream().anyMatch(
                                        file -> file.getStatus().equals(DownlinkStatus.ERROR)
                                ))
                        )
                )
        );
        if (hasError.get()) {
            edge.setStatus(DownlinkStatus.ERROR);
        } else {
            edge.setStatus(DownlinkStatus.DEPLOYED);
        }
    }

    private void downloadError(Throwable error, SignageEdgeEntity edge) {
        edge.setStatus(DownlinkStatus.ERROR);
        log.debug(error.getMessage());
    }

    private void deploy(SignageEdgeEntity edge) {
        edgeRepository.save(edge);
        edge.getProfiles().forEach(
                profile -> {
                    if (!edge.isEnable()) {
                        scheduleControlService.deleteSchedules(profile);
                    } else {
                        scheduleControlService.createSchedules(profile);
                    }
                }
        );
        styleService.checkUselessStyles();
        fileService.checkUselessFiles();
        this.eventPublisher.publishStatusEvent(this, edge.getStatus());
    }
}