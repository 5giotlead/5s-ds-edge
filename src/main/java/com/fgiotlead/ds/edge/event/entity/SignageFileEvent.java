package com.fgiotlead.ds.edge.event.entity;

import com.fgiotlead.ds.edge.model.entity.SignageFileEntity;
import com.fgiotlead.ds.edge.model.enumEntity.OperationType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SignageFileEvent extends ApplicationEvent {
    private final SignageFileEntity file;
    private final OperationType operationType;

    public SignageFileEvent(Object source, SignageFileEntity file, OperationType operationType) {
        super(source);
        this.file = file;
        this.operationType = operationType;
    }
}
