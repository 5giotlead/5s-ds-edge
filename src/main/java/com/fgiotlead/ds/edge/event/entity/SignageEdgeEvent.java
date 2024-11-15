package com.fgiotlead.ds.edge.event.entity;

import com.fgiotlead.ds.edge.model.entity.SignageEdgeEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SignageEdgeEvent extends ApplicationEvent {

    private final SignageEdgeEntity edge;

    public SignageEdgeEvent(Object source, SignageEdgeEntity edge) {
        super(source);
        this.edge = edge;
    }
}
