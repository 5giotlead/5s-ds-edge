package com.fgiotlead.ds.edge.util;

import com.fgiotlead.ds.edge.model.entity.schedule.RegularScheduleEntity;
import com.fgiotlead.ds.edge.model.entity.schedule.SignageScheduleEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class TimeUtils {

    static public boolean inSchedule(SignageScheduleEntity schedule) {
//        boolean isNormal = schedule instanceof RegularScheduleEntity;
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        LocalTime start = schedule.getStartTime();
        LocalTime end = schedule.getEndTime();
//        if (isNormal) {
        assert schedule instanceof RegularScheduleEntity;
        RegularScheduleEntity regularSchedule = (RegularScheduleEntity) schedule;
        Integer weekDay = regularSchedule.getWeekDay();
        return Objects.equals(weekDay, today.getDayOfWeek().getValue()) &&
                !now.isBefore(start) && now.isBefore(end);
//        } else {
//            PeriodSignageScheduleEntity periodSchedule = (PeriodSignageScheduleEntity) schedule;
//            LocalDate startDate = periodSchedule.getStartDate();
//            LocalDate endDate = periodSchedule.getEndDate();
//            return !today.isBefore(startDate) && !today.isAfter(endDate) && !now.isBefore(start) && now.isBefore(end);
//        }
    }

    static public boolean notInSchedule(SignageScheduleEntity schedule) {
//        boolean isNormal = schedule instanceof RegularScheduleEntity;
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        LocalTime start = schedule.getStartTime();
        LocalTime end = schedule.getEndTime();
//        if (isNormal) {
        assert schedule instanceof RegularScheduleEntity;
        RegularScheduleEntity regularSchedule = (RegularScheduleEntity) schedule;
        Integer weekDay = regularSchedule.getWeekDay();
        return !Objects.equals(weekDay, today.getDayOfWeek().getValue()) &&
                !now.isBefore(start) && now.isBefore(end);
//        } else {
//            PeriodSignageScheduleEntity periodSchedule = (PeriodSignageScheduleEntity) schedule;
//            LocalDate startDate = periodSchedule.getStartDate();
//            LocalDate endDate = periodSchedule.getEndDate();
//            return today.isBefore(startDate) || today.isAfter(endDate) ||
//                    (!now.isBefore(start) && now.isBefore(end));
//        }
    }
}
