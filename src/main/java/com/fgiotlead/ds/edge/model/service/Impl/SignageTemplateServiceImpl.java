package com.fgiotlead.ds.edge.model.service.Impl;


import com.fgiotlead.ds.edge.model.entity.SignageStyleEntity;
import com.fgiotlead.ds.edge.model.entity.SignageTemplateEntity;
import com.fgiotlead.ds.edge.model.entity.schedule.RegularScheduleEntity;
import com.fgiotlead.ds.edge.model.repository.SignageTemplateRepository;
import com.fgiotlead.ds.edge.model.service.SignageTemplateService;
import com.fgiotlead.ds.edge.quartz.model.service.ScheduleControlService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
@AllArgsConstructor
public class SignageTemplateServiceImpl implements SignageTemplateService {

    SignageTemplateRepository signageTemplateRepository;

    ScheduleControlService scheduleControlService;

    @Override
    public List<SignageTemplateEntity> findAll() {
        return signageTemplateRepository.findAll();
    }

    @Override
    public Optional<SignageTemplateEntity> findById(UUID id) {
        return signageTemplateRepository.findById(id);
    }

    @Override
    public ResponseEntity<Map<String, String>> create(SignageTemplateEntity template) {
        Map<String, String> responseEntity = new HashMap<>();
        if (signageTemplateRepository.findByName(template.getName()).isEmpty()) {
            signageTemplateRepository.save(template);
            responseEntity.put("message", "新增成功");
            return new ResponseEntity<>(responseEntity, HttpStatus.OK);
        } else {
            responseEntity.put("message", "新增失敗");
            responseEntity.put("state", "名稱重複");
            return new ResponseEntity<>(responseEntity, HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ResponseEntity<Map<String, String>> update(SignageTemplateEntity template) {
        Map<String, String> responseEntity = new HashMap<>();
        if (signageTemplateRepository.findById(template.getId()).isPresent()) {
            signageTemplateRepository.save(template);
            responseEntity.put("message", "修改成功");
            return new ResponseEntity<>(responseEntity, HttpStatus.OK);
        } else {
            responseEntity.put("message", "該版型不存在");
            return new ResponseEntity<>(responseEntity, HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void delete(SignageTemplateEntity template) {
        for (SignageStyleEntity style : template.getStyles()) {
            for (RegularScheduleEntity schedule : style.getSchedules()) {
                // 待處理
                scheduleControlService.deleteSchedule(schedule, schedule.getProfile());
            }
        }
        signageTemplateRepository.delete(template);
    }
}
