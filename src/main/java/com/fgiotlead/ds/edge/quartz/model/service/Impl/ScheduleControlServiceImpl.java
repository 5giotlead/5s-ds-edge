package com.fgiotlead.ds.edge.quartz.model.service.Impl;

import com.fgiotlead.ds.edge.event.entity.SignageScheduleEvent;
import com.fgiotlead.ds.edge.model.entity.SignageProfileEntity;
import com.fgiotlead.ds.edge.model.entity.schedule.RegularScheduleEntity;
import com.fgiotlead.ds.edge.model.entity.schedule.SignageScheduleEntity;
import com.fgiotlead.ds.edge.quartz.job.EndSignageJob;
import com.fgiotlead.ds.edge.quartz.job.StartSignageJob;
import com.fgiotlead.ds.edge.quartz.model.entity.SignageTriggerInfoEntity;
import com.fgiotlead.ds.edge.quartz.model.enumEntity.TriggerType;
import com.fgiotlead.ds.edge.quartz.model.service.ScheduleControlService;
import com.fgiotlead.ds.edge.quartz.model.service.SignageJobService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class ScheduleControlServiceImpl implements ScheduleControlService {

    SignageJobService signageJobService;
    @Autowired
    Scheduler scheduler;
    final String[] weekDays = {"*", "MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};

    @Override
    public void createSchedules(SignageProfileEntity signageProfile) {
        this.deleteSchedules(signageProfile);
        signageProfile.getSchedules().forEach(
                schedule -> {
                    List<SignageTriggerInfoEntity> triggerInfoList = packageTriggerInfo(signageProfile, schedule);
                    triggerInfoList.forEach(this::saveTrigger);
                }
        );
    }

    @Override
    public void deleteSchedules(SignageProfileEntity signageProfile) {
        GroupMatcher<TriggerKey> groupMatcher = GroupMatcher.triggerGroupEquals(signageProfile.getId().toString());
        try {
            List<TriggerKey> triggerKeys = new ArrayList<>(scheduler.getTriggerKeys(groupMatcher));
            scheduler.unscheduleJobs(triggerKeys);
            signageJobService.setToDefault(signageProfile.getId());
        } catch (SchedulerException e) {
            log.warn(e.getMessage());
        }
    }

    @Override
    public void saveRegularSchedule(RegularScheduleEntity regularSchedule, SignageProfileEntity signageProfile) {
        List<SignageTriggerInfoEntity> triggerInfoList = packageTriggerInfo(signageProfile, regularSchedule);
        triggerInfoList.forEach(this::saveTrigger);
    }

    private List<SignageTriggerInfoEntity> packageTriggerInfo(
            SignageProfileEntity signageProfile,
            RegularScheduleEntity schedule
    ) {
        List<SignageTriggerInfoEntity> triggersInfo = new ArrayList<>();
        String cronStartTime = "0 ";
        String cronEndTime = "0 ";
        String weekDay;
        weekDay = weekDays[schedule.getWeekDay()];
        cronStartTime += schedule.getStartTime().getMinute() +
                " " + schedule.getStartTime().getHour() + " ? * " + weekDay;
        cronEndTime += schedule.getEndTime().getMinute() +
                " " + schedule.getEndTime().getHour() + " ? * " + weekDay;
        TriggerKey startKey = TriggerKey.triggerKey(
                schedule.getId() + "_start", signageProfile.getId().toString()
        );
        TriggerKey endKey = TriggerKey.triggerKey(
                schedule.getId() + "_end", signageProfile.getId().toString()
        );
        SignageTriggerInfoEntity startTriggerInfo = SignageTriggerInfoEntity
                .builder()
                .deviceId(signageProfile.getId())
                .scheduleId(schedule.getId())
                .styleId(schedule.getStyle().getId())
                .type(TriggerType.START)
                .triggerKey(startKey)
                .cronTime(cronStartTime)
                .build();
        SignageTriggerInfoEntity endTriggerInfo = SignageTriggerInfoEntity
                .builder()
                .deviceId(signageProfile.getId())
                .scheduleId(schedule.getId())
                .styleId(schedule.getStyle().getId())
                .type(TriggerType.END)
                .triggerKey(endKey)
                .cronTime(cronEndTime)
                .build();
        triggersInfo.add(startTriggerInfo);
        triggersInfo.add(endTriggerInfo);
        return triggersInfo;
    }

    @Override
    public void deleteSchedule(RegularScheduleEntity schedule, SignageProfileEntity profile) {
        List<SignageTriggerInfoEntity> triggerInfoList = packageTriggerInfo(profile, schedule);
        if (!triggerInfoList.isEmpty()) {
            triggerInfoList.forEach(
                    triggerInfo -> deleteTrigger(triggerInfo, schedule)
            );
        }
    }

    @EventListener(condition = "#scheduleEvent.operationType.name == 'DELETE'")
    public void deleteSchedule(SignageScheduleEvent scheduleEvent) {
        RegularScheduleEntity schedule = scheduleEvent.getSchedule();
        List<SignageTriggerInfoEntity> triggerInfoList = packageTriggerInfo(schedule.getProfile(), schedule);
        if (!triggerInfoList.isEmpty()) {
            triggerInfoList.forEach(
                    triggerInfo -> deleteTrigger(triggerInfo, schedule)
            );
        }
    }

    private void saveTrigger(SignageTriggerInfoEntity triggerInfo) {
        TriggerKey triggerKey = triggerInfo.getTriggerKey();
        String deviceId = triggerInfo.getDeviceId().toString();
        String scheduleId = triggerInfo.getScheduleId().toString();
        String styleId = triggerInfo.getStyleId().toString();
        TriggerType type = triggerInfo.getType();
        boolean isStart = type.equals(TriggerType.START);
        try {
            JobDetail jobDetail = JobBuilder
                    .newJob(isStart ? StartSignageJob.class : EndSignageJob.class)
                    // JobDetail 是否要持久化 ( 若 false, 對應的 trigger 移除時會一併刪除 )
                    .storeDurably(false)
                    .usingJobData("deviceId", deviceId)
                    .usingJobData("scheduleId", scheduleId)
                    .usingJobData("styleId", styleId)
                    .build();
            Trigger trigger;
            trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(triggerKey)
                    .withPriority(isStart ? 5 : 6)
                    .startNow()
                    .withSchedule(
                            CronScheduleBuilder
                                    .cronSchedule(triggerInfo.getCronTime())
                                    .withMisfireHandlingInstructionFireAndProceed()
                    )
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
            signageJobService.checkSchedule(deviceId, scheduleId, styleId, type, "save");
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    private void deleteTrigger(SignageTriggerInfoEntity triggerInfo, SignageScheduleEntity schedule) {
        TriggerKey triggerKey = triggerInfo.getTriggerKey();
        UUID deviceId = triggerInfo.getDeviceId();
        TriggerType type = triggerInfo.getType();
        try {
            if (scheduler.checkExists(triggerKey)) {
                scheduler.unscheduleJob(triggerKey);
                signageJobService.deleteSchedule(deviceId, schedule, type);
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}