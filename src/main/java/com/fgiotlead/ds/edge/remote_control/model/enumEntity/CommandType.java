package com.fgiotlead.ds.edge.remote_control.model.enumEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommandType {

    POWER_ON(0),
    POWER_OFF(1),
    POWER_STATUS(2);

    private final int code;
}
