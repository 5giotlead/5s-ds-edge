package com.fgiotlead.ds.edge.controller;

import com.fgiotlead.ds.edge.model.entity.SignageStyleEntity;
import com.fgiotlead.ds.edge.model.service.SignageStyleService;
import com.fgiotlead.ds.edge.remote_control.model.enumEntity.CommandType;
import com.fgiotlead.ds.edge.remote_control.model.service.DisplayControlService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/signage")
@CrossOrigin("*")
@AllArgsConstructor
public class SignageController {

    private SignageStyleService styleService;
    private DisplayControlService displayControlService;


//    @GetMapping("edge")
//    public List<SignageEdgeEntity> findList() {
//        return edgeService.findAll();
//    }

//    @PostMapping("edge")
//    public void save(@RequestBody SignageEdgeEntity edge) {
//        edgeService.save(edge);
//    }

//    @PostMapping("block")
//    public void save(@RequestBody SignageBlockEntity block) {
//        blockService.save(block);
//    }
//
//    @PostMapping("file")
//    public void save(@RequestBody SignageFileEntity file) {
//        fileService.save(file);
//    }

//    @DeleteMapping("file/{fileId}")
//    public void deleteById(@PathVariable UUID fileId) {
//        fileService.deleteById(fileId);
//    }

    @GetMapping("style/{styleId}")
    public ResponseEntity<SignageStyleEntity> findById(@PathVariable UUID styleId) {
        Optional<SignageStyleEntity> styleEntityOptional = styleService.findById(styleId);
        return styleEntityOptional
                .map(signageStyleEntity -> new ResponseEntity<>(signageStyleEntity, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("control/{displayId}/power:on")
    public ResponseEntity<Map<String, String>> powerOn(@PathVariable int displayId) {
        return displayControlService.remoteControl(CommandType.POWER_ON, displayId);
    }

    @PostMapping("control/{displayId}/power:off")
    public ResponseEntity<Map<String, String>> powerOff(@PathVariable int displayId) {
        return displayControlService.remoteControl(CommandType.POWER_OFF, displayId);
    }
}