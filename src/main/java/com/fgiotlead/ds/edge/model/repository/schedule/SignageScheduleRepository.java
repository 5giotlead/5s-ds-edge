package com.fgiotlead.ds.edge.model.repository.schedule;

import com.fgiotlead.ds.edge.model.entity.schedule.SignageScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SignageScheduleRepository extends JpaRepository<SignageScheduleEntity, UUID> {
}
