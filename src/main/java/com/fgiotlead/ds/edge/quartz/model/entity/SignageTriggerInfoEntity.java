package com.fgiotlead.ds.edge.quartz.model.entity;

import com.fgiotlead.ds.edge.quartz.model.enumEntity.TriggerType;
import lombok.Builder;
import lombok.Data;
import org.quartz.TriggerKey;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class SignageTriggerInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID deviceId;
    private UUID scheduleId;
    private UUID styleId;
    private boolean isPeriod;
    private TriggerType type;
    private TriggerKey triggerKey;
    private LocalDate startDate;
    private LocalDate endDate;
    private String cronTime;
}
