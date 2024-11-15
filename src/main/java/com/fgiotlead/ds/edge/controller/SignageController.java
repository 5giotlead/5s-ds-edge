package com.fgiotlead.ds.edge.controller;

import com.fgiotlead.ds.edge.model.entity.SignageStyleEntity;
import com.fgiotlead.ds.edge.model.service.SignageStyleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/signage")
@CrossOrigin("*")
@AllArgsConstructor
public class SignageController {

    private SignageStyleService styleService;


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
}