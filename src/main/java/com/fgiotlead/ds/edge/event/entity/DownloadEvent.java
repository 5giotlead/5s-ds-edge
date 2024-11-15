package com.fgiotlead.ds.edge.event.entity;

import com.fgiotlead.ds.edge.model.entity.SignageFileEntity;
import com.fgiotlead.ds.edge.model.enumEntity.DownlinkStatus;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DownloadEvent extends ApplicationEvent {

    private final SignageFileEntity file;
    private final DownlinkStatus status;

    public DownloadEvent(Object source, SignageFileEntity file, DownlinkStatus status) {
        super(source);
        this.file = file;
        this.status = status;
    }
}
