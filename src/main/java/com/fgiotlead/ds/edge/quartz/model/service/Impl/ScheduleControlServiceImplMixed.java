package com.fgiotlead.ds.edge.quartz.model.service.Impl;//package com.fgiotlead.ds_edge.quartz.model.service.Impl;
//
//import com.fgiotlead.ds_edge.model.entity.SignageProfileEntity;
//import com.fgiotlead.ds_edge.model.entity.schedule.RegularScheduleEntity;
//import com.fgiotlead.ds_edge.model.entity.schedule.SignageScheduleEntity;
//import com.fgiotlead.ds_edge.quartz.job.EndSignageJob;
//import com.fgiotlead.ds_edge.quartz.job.StartSignageJob;
//import com.fgiotlead.ds_edge.quartz.model.entity.SignageTriggerInfoEntity;
//import com.fgiotlead.ds_edge.quartz.model.enumEntity.TriggerType;
//import com.fgiotlead.ds_edge.quartz.model.service.ScheduleControlService;
//import com.fgiotlead.ds_edge.quartz.model.service.SignageJobService;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.quartz.*;
//import org.quartz.impl.matchers.GroupMatcher;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//@Slf4j
//@Service
//@AllArgsConstructor
//public class ScheduleControlServiceImpl_Mixed implements ScheduleControlService {
//
//    SignageJobService signageJobService;
//    @Autowired
//    Scheduler scheduler;
//    final String[] weekDays = {"*", "MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
//
//    @Override
//    public void createSchedules(SignageProfileEntity signageProfile) {
//        signageProfile.getSchedules().forEach(
//                schedule -> {
//                    List<SignageTriggerInfoEntity> triggerInfoList = packageTriggerInfo(signageProfile, schedule);
//                    triggerInfoList.forEach(triggerInfo -> saveTrigger(triggerInfo, schedule));
//                }
//        );
//    }
//
//    @Override
//    public void deleteSchedules(SignageProfileEntity signageProfile) {
//        GroupMatcher<TriggerKey> groupMatcher = GroupMatcher.triggerGroupEquals(signageProfile.getId().toString());
//        try {
//            List<TriggerKey> triggerKeys = new ArrayList<>(scheduler.getTriggerKeys(groupMatcher));
//            scheduler.unscheduleJobs(triggerKeys);
//            signageJobService.setToDefault(signageProfile.getId());
//        } catch (SchedulerException e) {
//            log.warn(e.getMessage());
//        }
//    }
//
//    @Override
//    public void saveRegularSchedule(RegularScheduleEntity regularSchedule, SignageProfileEntity signageProfile) {
//        List<SignageTriggerInfoEntity> triggerInfoList = packageTriggerInfo(signageProfile, regularSchedule);
//        triggerInfoList.forEach(triggerInfo -> saveTrigger(triggerInfo, regularSchedule)
//        );
//    }
//
////    @Override
////    public void savePeriodSchedule(PeriodSignageScheduleEntity periodSchedule, SignageProfileEntity signageProfile) {
////        List<SignageTriggerInfoEntity> triggerInfoList = packageTriggerInfo(signageProfile, periodSchedule);
////        triggerInfoList.forEach(triggerInfo -> saveTrigger(triggerInfo, periodSchedule);
////    }
//
//    private List<SignageTriggerInfoEntity> packageTriggerInfo(
//            SignageProfileEntity signageProfile,
//            SignageScheduleEntity schedule
//    ) {
//        List<SignageTriggerInfoEntity> triggersInfo = new ArrayList<>();
//        boolean isRegular = schedule instanceof RegularScheduleEntity;
////        boolean isPeriod = schedule instanceof PeriodSignageScheduleEntity;
//        String cronStartTime = "0 ";
//        String cronEndTime = "0 ";
//        String weekDay;
//        if (isRegular) {
//            weekDay = weekDays[((RegularScheduleEntity) schedule).getWeekDay()];
//        } else {
//            weekDay = weekDays[0];
//        }
//        cronStartTime += schedule.getStartTime().getMinute() +
//                " " + schedule.getStartTime().getHour() + " ? * " + weekDay;
//        cronEndTime += schedule.getEndTime().getMinute() +
//                " " + schedule.getEndTime().getHour() + " ? * " + weekDay;
//        TriggerKey startKey = TriggerKey.triggerKey(
//                schedule.getId() + "_start", signageProfile.getId().toString()
//        );
//        TriggerKey endKey = TriggerKey.triggerKey(
//                schedule.getId() + "_end", signageProfile.getId().toString()
//        );
//        SignageTriggerInfoEntity startTriggerInfo = SignageTriggerInfoEntity
//                .builder()
//                .deviceId(signageProfile.getId())
//                .scheduleId(schedule.getId())
//                .styleId(schedule.getStyle().getId())
//                .type(TriggerType.START)
//                .triggerKey(startKey)
////                .startDate(isPeriod ? ((PeriodSignageScheduleEntity) schedule).getStartDate() : null)
////                .endDate(isPeriod ? ((PeriodSignageScheduleEntity) schedule).getEndDate() : null)
//                .cronTime(cronStartTime)
//                .build();
//        SignageTriggerInfoEntity endTriggerInfo = SignageTriggerInfoEntity
//                .builder()
//                .deviceId(signageProfile.getId())
//                .scheduleId(schedule.getId())
//                .styleId(schedule.getStyle().getId())
//                .type(TriggerType.END)
//                .triggerKey(endKey)
////                .startDate(isPeriod ? ((PeriodSignageScheduleEntity) schedule).getStartDate() : null)
////                .endDate(isPeriod ? ((PeriodSignageScheduleEntity) schedule).getEndDate() : null)
//                .cronTime(cronEndTime)
//                .build();
//        triggersInfo.add(startTriggerInfo);
//        triggersInfo.add(endTriggerInfo);
//        return triggersInfo;
//    }
//
//    @Override
//    public void deleteSchedule(SignageScheduleEntity schedule, SignageProfileEntity profile) {
//        //        if (schedule instanceof NormalSignageScheduleEntity) {
//        RegularScheduleEntity regularSchedule = (RegularScheduleEntity) schedule;
//        List<SignageTriggerInfoEntity> triggerInfoList = packageTriggerInfo(profile, regularSchedule);
////        } else if (schedule instanceof PeriodSignageScheduleEntity) {
////            PeriodSignageScheduleEntity periodSchedule = (PeriodSignageScheduleEntity) schedule;
////            triggerInfoList.addAll(packageTriggerInfo(signageProfile, periodSchedule));
////        }
//        if (!triggerInfoList.isEmpty()) {
//            triggerInfoList.forEach(
//                    triggerInfo -> deleteTrigger(triggerInfo, schedule)
//            );
//        }
//    }
//
//    private void saveTrigger(SignageTriggerInfoEntity triggerInfo, SignageScheduleEntity schedule) {
////        boolean isPeriod = schedule instanceof PeriodSignageScheduleEntity;
//        ZoneId zoneId = ZoneId.systemDefault();
//        TriggerKey triggerKey = triggerInfo.getTriggerKey();
//        LocalDate startDate = triggerInfo.getStartDate();
//        LocalDate endDate = triggerInfo.getEndDate();
//        String deviceId = triggerInfo.getDeviceId().toString();
//        String scheduleId = triggerInfo.getScheduleId().toString();
//        String styleId = triggerInfo.getStyleId().toString();
//        TriggerType type = triggerInfo.getType();
//        boolean isStart = type.equals(TriggerType.START);
//        try {
//            JobDetail jobDetail = JobBuilder
//                    .newJob(isStart ? StartSignageJob.class : EndSignageJob.class)
//                    // JobDetail 是否要持久化 ( 若 false, 對應的 trigger 移除時會一併刪除 )
//                    .storeDurably(false)
//                    .usingJobData("deviceId", deviceId)
//                    .usingJobData("scheduleId", scheduleId)
//                    .usingJobData("styleId", styleId)
//                    .build();
//            Trigger trigger;
////            if (isPeriod) {
////                trigger = TriggerBuilder
////                        .newTrigger()
////                        .withIdentity(triggerKey)
////                        .withPriority(Objects.equals(type, "start") ? 5 : 6)
////                        .startAt(Date.from(startDate.atStartOfDay(zoneId).toInstant()))
////                        .endAt(Date.from(endDate.plusDays(1).atStartOfDay(zoneId).toInstant()))
////                        .withSchedule(
////                                CronScheduleBuilder
////                                        .cronSchedule(triggerInfo.getCronTime())
////                                        .withMisfireHandlingInstructionFireAndProceed()
////                        )
////                        .build();
////
////            } else {
//            trigger = TriggerBuilder
//                    .newTrigger()
//                    .withIdentity(triggerKey)
//                    .withPriority(isStart ? 5 : 6)
//                    .startNow()
//                    .withSchedule(
//                            CronScheduleBuilder
//                                    .cronSchedule(triggerInfo.getCronTime())
//                                    .withMisfireHandlingInstructionFireAndProceed()
//                    )
//                    .build();
////            }
//            scheduler.scheduleJob(jobDetail, trigger);
//            signageJobService.checkSchedule(deviceId, scheduleId, styleId, type, "save");
//        } catch (Exception e) {
//            log.warn(e.getMessage());
//        }
//    }
//
//    private void deleteTrigger(SignageTriggerInfoEntity triggerInfo, SignageScheduleEntity schedule) {
//        TriggerKey triggerKey = triggerInfo.getTriggerKey();
//        UUID deviceId = triggerInfo.getDeviceId();
//        TriggerType type = triggerInfo.getType();
//        try {
//            if (scheduler.checkExists(triggerKey)) {
//                scheduler.unscheduleJob(triggerKey);
//                signageJobService.deleteSchedule(deviceId, schedule, type);
//            }
//        } catch (Exception e) {
//            log.warn(e.getMessage());
//        }
//    }
//}