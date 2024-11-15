package com.fgiotlead.ds.edge.quartz.model.service;

import com.fgiotlead.ds.edge.model.entity.schedule.SignageScheduleEntity;
import com.fgiotlead.ds.edge.quartz.model.enumEntity.TriggerType;

import java.util.UUID;

public interface SignageJobService {

    void checkSchedule(String deviceId, String scheduleId, String styleId, TriggerType type, String action);

    void deleteSchedule(UUID deviceId, SignageScheduleEntity schedule, TriggerType type);

    void setToDefault(UUID deviceId);

    void changeStyle(String deviceId, String scheduleId, String styleId, TriggerType type);

    void refresh(String deviceId);
}
