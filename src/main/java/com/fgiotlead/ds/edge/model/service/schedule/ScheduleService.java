package com.fgiotlead.ds.edge.model.service.schedule;

import com.fgiotlead.ds.edge.model.entity.schedule.SignageScheduleEntity;

import java.time.LocalTime;

public interface ScheduleService<T extends SignageScheduleEntity> {

    void findInSchedule(Integer currentWeekDay, LocalTime currentTime);

    void checkUselessSchedules();

}
