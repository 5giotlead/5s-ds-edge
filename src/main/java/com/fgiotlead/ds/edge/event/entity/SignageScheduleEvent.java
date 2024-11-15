package com.fgiotlead.ds.edge.event.entity;

import com.fgiotlead.ds.edge.model.entity.schedule.RegularScheduleEntity;
import com.fgiotlead.ds.edge.model.enumEntity.OperationType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SignageScheduleEvent extends ApplicationEvent {

    private final RegularScheduleEntity schedule;
    private final OperationType operationType;


    public SignageScheduleEvent(Object source, RegularScheduleEntity schedule, OperationType operationType) {
        super(source);
        this.schedule = schedule;
        this.operationType = operationType;
    }
}
