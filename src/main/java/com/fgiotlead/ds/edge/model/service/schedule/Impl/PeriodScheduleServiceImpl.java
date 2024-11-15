package com.fgiotlead.ds.edge.model.service.schedule.Impl;//package com.fgiotlead.ds_edge.model.service.schedule.Impl;
//
//
//import com.fgiotlead.ds_edge.model.entity.schedule.PeriodSignageScheduleEntity;
//import com.fgiotlead.ds_edge.model.repository.schedule.PeriodScheduleRepository;
//import com.fgiotlead.ds_edge.model.service.schedule.ScheduleService;
//import com.fgiotlead.ds_edge.quartz.service.ScheduleControlService;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//@Service
//@Transactional(rollbackFor = Exception.class)
//@AllArgsConstructor
//public class PeriodScheduleServiceImpl implements ScheduleService<PeriodSignageScheduleEntity> {
//    PeriodScheduleRepository periodScheduleRepository;
//    ScheduleControlService scheduleControlService;
//
//    @Override
//    public void afterPersist(PeriodSignageScheduleEntity periodSchedule) {
////        scheduleControlService.savePeriodSchedule(periodSchedule);
//    }
//
//    @Override
//    public void beforeUpdate(PeriodSignageScheduleEntity periodSchedule) {
//        Optional<PeriodSignageScheduleEntity> periodScheduleOptional = periodScheduleRepository.findById(periodSchedule.getId());
////        periodScheduleOptional.ifPresent(existPeriodSchedule ->
////                scheduleControlService.deleteSchedule(existPeriodSchedule)
////        );
//    }
//
//    @Override
//    public void afterUpdate(PeriodSignageScheduleEntity periodSchedule) {
////        scheduleControlService.savePeriodSchedule(periodSchedule);
//    }
//
//    @Override
//    public void afterRemove(PeriodSignageScheduleEntity periodSchedule) {
////        scheduleControlService.deleteSchedule(periodSchedule);
//    }
//}
