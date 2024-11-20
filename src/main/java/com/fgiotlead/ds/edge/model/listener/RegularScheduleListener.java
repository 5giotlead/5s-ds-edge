package com.fgiotlead.ds.edge.model.listener;

import com.fgiotlead.ds.edge.event.entity.SignageScheduleEvent;
import com.fgiotlead.ds.edge.model.entity.schedule.RegularScheduleEntity;
import com.fgiotlead.ds.edge.model.enumEntity.OperationType;
import jakarta.persistence.PostRemove;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

@AllArgsConstructor
public class RegularScheduleListener {

    ApplicationEventPublisher applicationEventPublisher;

    @PostRemove
    public void afterRemove(RegularScheduleEntity schedule) {
        applicationEventPublisher.publishEvent(new SignageScheduleEvent(this, schedule, OperationType.DELETE));
    }
}
