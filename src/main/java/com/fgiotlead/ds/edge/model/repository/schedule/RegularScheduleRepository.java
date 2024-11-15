package com.fgiotlead.ds.edge.model.repository.schedule;

import com.fgiotlead.ds.edge.model.entity.schedule.RegularScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface RegularScheduleRepository extends JpaRepository<RegularScheduleEntity, UUID> {

    @Query(value = "SELECT ns FROM RegularScheduleEntity ns " +
            "WHERE ns.weekDay = ?1 " +
            "AND ns.startTime <= ?2 " +
            "AND ns.endTime > ?2"
    )
    List<RegularScheduleEntity> findInSchedule(
            Integer currentWeekDay,
            LocalTime currentTime
    );
}
