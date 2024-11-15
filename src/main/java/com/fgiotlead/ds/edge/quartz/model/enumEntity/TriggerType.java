package com.fgiotlead.ds.edge.quartz.model.enumEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TriggerType {

    START(0),
    END(1);

    private final int code;
}
