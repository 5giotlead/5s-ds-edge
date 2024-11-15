package com.fgiotlead.ds.edge.event.entity;

import com.fgiotlead.ds.edge.model.enumEntity.DownlinkStatus;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class StatusEvent extends ApplicationEvent {

    private final DownlinkStatus status;

    public StatusEvent(Object source, DownlinkStatus status) {
        super(source);
        this.status = status;
    }
}
