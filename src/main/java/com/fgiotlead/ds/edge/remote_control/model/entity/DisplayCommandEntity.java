package com.fgiotlead.ds.edge.remote_control.model.entity;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DisplayCommandEntity {

    private char length;
    private char cmdType;
    private int cmdCode;
    private char value1;
    private char value2;
    private char value3;
}
