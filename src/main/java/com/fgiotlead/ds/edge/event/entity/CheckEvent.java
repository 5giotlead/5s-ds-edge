package com.fgiotlead.ds.edge.event.entity;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CheckEvent extends ApplicationEvent {

    public CheckEvent(Object source) {
        super(source);
    }
}
