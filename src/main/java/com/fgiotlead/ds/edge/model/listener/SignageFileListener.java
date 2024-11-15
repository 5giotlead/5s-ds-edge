package com.fgiotlead.ds.edge.model.listener;

import com.fgiotlead.ds.edge.event.entity.SignageFileEvent;
import com.fgiotlead.ds.edge.model.entity.SignageFileEntity;
import com.fgiotlead.ds.edge.model.enumEntity.OperationType;
import jakarta.persistence.PostRemove;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

@AllArgsConstructor
public class SignageFileListener {

    ApplicationEventPublisher applicationEventPublisher;

    @PostRemove
    public void afterRemove(SignageFileEntity file) {
        applicationEventPublisher.publishEvent(new SignageFileEvent(this, file, OperationType.DELETE));
    }
}
