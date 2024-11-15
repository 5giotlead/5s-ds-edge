package com.fgiotlead.ds.edge.event.entity;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Set;
import java.util.UUID;

@Getter
public class RefreshEvent extends ApplicationEvent {

    private final Set<UUID> devicesId;

    public RefreshEvent(Object source, Set<UUID> devicesId) {
        super(source);
        this.devicesId = devicesId;
    }
}
