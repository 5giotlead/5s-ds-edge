package com.fgiotlead.ds.edge.event;

import com.fgiotlead.ds.edge.event.entity.SignageEdgeEvent;
import com.fgiotlead.ds.edge.event.entity.StatusEvent;
import com.fgiotlead.ds.edge.model.entity.SignageEdgeEntity;
import com.fgiotlead.ds.edge.model.enumEntity.DownlinkStatus;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SignageEventPublisher {

    private ApplicationEventPublisher applicationEventPublisher;

    public void publishEdgeEvent(Object source, SignageEdgeEntity signageEdge) {
        applicationEventPublisher.publishEvent(new SignageEdgeEvent(source, signageEdge));
    }

    public void publishStatusEvent(Object source, DownlinkStatus status) {
        applicationEventPublisher.publishEvent(new StatusEvent(source, status));
    }
}