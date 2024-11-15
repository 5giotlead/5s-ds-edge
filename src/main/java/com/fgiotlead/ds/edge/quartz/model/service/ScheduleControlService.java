package com.fgiotlead.ds.edge.quartz.model.service;

import com.fgiotlead.ds.edge.model.entity.SignageProfileEntity;
import com.fgiotlead.ds.edge.model.entity.schedule.RegularScheduleEntity;

public interface ScheduleControlService {

    void createSchedules(SignageProfileEntity signageProfile);

    void deleteSchedules(SignageProfileEntity signageProfile);

    void saveRegularSchedule(RegularScheduleEntity regularSchedule, SignageProfileEntity signageProfile);

    void deleteSchedule(RegularScheduleEntity schedule, SignageProfileEntity profile);
}
