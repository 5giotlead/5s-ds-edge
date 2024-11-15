package com.fgiotlead.ds.edge.rsocket.controller;

import com.fgiotlead.ds.edge.model.entity.SignageEdgeEntity;
import com.fgiotlead.ds.edge.model.service.SignageEdgeService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@MessageMapping("edge")
@AllArgsConstructor
public class RsocketController {

    private SignageEdgeService signageEdgeService;

    @MessageMapping("publish")
    public Mono<Void> publish(SignageEdgeEntity edge) {
        signageEdgeService.downloadSettings(edge);
        return Mono.empty();
    }
}
