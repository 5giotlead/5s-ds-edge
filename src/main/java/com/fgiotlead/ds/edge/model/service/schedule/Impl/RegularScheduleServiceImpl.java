package com.fgiotlead.ds.edge.model.service.schedule.Impl;


import com.fgiotlead.ds.edge.model.entity.schedule.RegularScheduleEntity;
import com.fgiotlead.ds.edge.model.repository.schedule.RegularScheduleRepository;
import com.fgiotlead.ds.edge.model.service.schedule.ScheduleService;
import com.fgiotlead.ds.edge.quartz.model.enumEntity.TriggerType;
import com.fgiotlead.ds.edge.quartz.model.service.SignageJobService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@AllArgsConstructor
public class RegularScheduleServiceImpl implements ScheduleService<RegularScheduleEntity> {

    RegularScheduleRepository regularScheduleRepository;
    SignageJobService signageJobService;

    @Override
    public void findInSchedule(Integer currentWeekDay, LocalTime currentTime) {
        List<RegularScheduleEntity> scheduleList = regularScheduleRepository.findInSchedule(currentWeekDay, currentTime);
        if (!scheduleList.isEmpty()) {
            RegularScheduleEntity regularSchedule = scheduleList.get(0);
            signageJobService.changeStyle(
                    regularSchedule.getProfile().getId().toString(),
                    regularSchedule.getId().toString(),
                    regularSchedule.getStyle().getId().toString(),
                    TriggerType.START
            );
        }
    }

    @Override
    public void checkUselessSchedules() {
        List<RegularScheduleEntity> existSchedules = regularScheduleRepository.findAll();
    }
}
