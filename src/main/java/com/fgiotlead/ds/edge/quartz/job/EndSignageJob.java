package com.fgiotlead.ds.edge.quartz.job;

import com.fgiotlead.ds.edge.quartz.model.enumEntity.TriggerType;
import com.fgiotlead.ds.edge.quartz.model.service.SignageJobService;
import lombok.AllArgsConstructor;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

@AllArgsConstructor
public class EndSignageJob extends QuartzJobBean {
    SignageJobService signageJobService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String deviceId = jobDataMap.getString("deviceId");
        String scheduleId = jobDataMap.getString("scheduleId");
        String styleId = jobDataMap.getString("styleId");
        signageJobService.checkSchedule(deviceId, scheduleId, styleId, TriggerType.END, "job");
    }
}
