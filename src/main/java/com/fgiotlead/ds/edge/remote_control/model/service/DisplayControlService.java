package com.fgiotlead.ds.edge.remote_control.model.service;

import com.fgiotlead.ds.edge.remote_control.model.enumEntity.CommandType;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface DisplayControlService {

    ResponseEntity<Map<String, String>> remoteControl(CommandType commandType, int deviceId);
}
