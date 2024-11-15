package com.fgiotlead.ds.edge.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fgiotlead.ds.edge.model.entity.schedule.RegularScheduleEntity;
import com.fgiotlead.ds.edge.model.service.schedule.ScheduleService;
import com.fgiotlead.ds.edge.rsocket.model.service.RSocketService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;


@Component
public class DigitalSignageInit {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    RSocketService rSocketService;
    @Autowired
    ScheduleService<RegularScheduleEntity> regularScheduleService;

    @PostConstruct
    public void init() {
        regularScheduleService.findInSchedule(LocalDate.now().getDayOfWeek().getValue(), LocalTime.now());
        rSocketService.register();
    }

    @PostConstruct
    public void registerJsonModule() {
        objectMapper.registerModule(new JavaTimeModule());
    }

}
