package com.fgiotlead.ds.edge.quartz.model.service.Impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fgiotlead.ds.edge.model.entity.schedule.RegularScheduleEntity;
import com.fgiotlead.ds.edge.model.entity.schedule.SignageScheduleEntity;
import com.fgiotlead.ds.edge.model.repository.schedule.SignageScheduleRepository;
import com.fgiotlead.ds.edge.quartz.model.enumEntity.TriggerType;
import com.fgiotlead.ds.edge.quartz.model.service.SignageJobService;
import com.fgiotlead.ds.edge.tb.model.entity.TbDeviceCredentialsEntity;
import com.fgiotlead.ds.edge.tb.model.service.TbDeviceCredentialsService;
import com.fgiotlead.ds.edge.util.TimeUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
@Transactional
public class SignageJobServiceImpl implements SignageJobService {

    @Value("${thingsboard.address}")
    private String host;
    @Value("${thingsboard.port}")
    private String port;
    @Value("${thingsboard.ssl.enabled}")
    private boolean isSecure;

    @Autowired
    private SignageScheduleRepository signageScheduleRepository;
    @Autowired
    private TbDeviceCredentialsService tbDeviceCredentialsService;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void checkSchedule(String deviceId, String scheduleId, String styleId, TriggerType type, String action) {
        Optional<SignageScheduleEntity> schedule = signageScheduleRepository.findById(UUID.fromString(scheduleId));
        if (schedule.isPresent()) {
            switch (action) {
                case "job":
                case "save":
                    if (type.equals(TriggerType.START) && TimeUtils.inSchedule(schedule.get())) {
                        this.changeStyle(deviceId, scheduleId, styleId, type);
                    } else if (type.equals(TriggerType.END) && !TimeUtils.inSchedule(schedule.get())) {
                        List<RegularScheduleEntity> schedules = schedule.get().getProfile().getSchedules()
                                .stream().filter(TimeUtils::inSchedule).toList();
                        if (schedules.isEmpty()) {
                            this.changeStyle(deviceId, scheduleId, styleId, type);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void deleteSchedule(UUID deviceId, SignageScheduleEntity schedule, TriggerType type) {
        if (type.equals(TriggerType.END) && TimeUtils.inSchedule(schedule)) {
            List<RegularScheduleEntity> schedules =
                    schedule.getProfile().getSchedules()
                            .stream().filter(TimeUtils::inSchedule).toList();
            if (schedules.isEmpty()) {
                changeStyle(
                        deviceId.toString(),
                        schedule.getId().toString(),
                        schedule.getStyle().getId().toString(),
                        type);
            }
        }
    }

    @Override
    public void setToDefault(UUID deviceId) {
        Optional<TbDeviceCredentialsEntity> credentialsEntity = tbDeviceCredentialsService.findByDeviceId(deviceId);
        if (credentialsEntity.isPresent() && credentialsEntity.get().getCredentialsType().equals("ACCESS_TOKEN")) {
            String accessToken = credentialsEntity.get().getCredentialsId();
            String attributesUri =
                    isSecure ? "https" : "http" + "://" + host + ":" + port + "/api/v1/" + accessToken + "/attributes";
            Map<String, String> requestMap = new HashMap<>();
            requestMap.put("Schedule", "default");
            requestMap.put("Style", "default");
            setAttributes(attributesUri, requestMap);
        }
    }

    @Override
    public void changeStyle(String deviceId, String scheduleId, String styleId, TriggerType type) {
        UUID id = UUID.fromString(deviceId);
        Optional<TbDeviceCredentialsEntity> credentialsEntity = tbDeviceCredentialsService.findByDeviceId(id);
        if (credentialsEntity.isPresent() && credentialsEntity.get().getCredentialsType().equals("ACCESS_TOKEN")) {
            String accessToken = credentialsEntity.get().getCredentialsId();
            String attributesUri =
                    isSecure ? "https" : "http" + "://" + host + ":" + port + "/api/v1/" + accessToken + "/attributes";
            JsonNode jsonNode = restTemplate
                    .getForObject(attributesUri + "?clientKeys=Schedule,Style", JsonNode.class);
            Map<String, String> requestMap = new HashMap<>();
            if (jsonNode != null && !jsonNode.get("client").isEmpty()) {
                JsonNode clientAttributes = jsonNode.get("client");
                String currentScheduleId = "";
                String currentStyleId = "";
                if (clientAttributes.get("Schedule") != null) {
                    currentScheduleId = clientAttributes.get("Schedule").textValue();
                }
                if (clientAttributes.get("Style") != null) {
                    currentStyleId = clientAttributes.get("Style").textValue();
                }
                boolean isSameJob = scheduleId.equals(currentScheduleId) && styleId.equals(currentStyleId);
                if (type.equals(TriggerType.START)) {
                    requestMap.put("Schedule", scheduleId);
                    requestMap.put("Style", styleId);
                } else {
                    if (isSameJob) {
                        requestMap.put("Schedule", "default");
                        requestMap.put("Style", "default");
                    }
                }
            } else {
                if (type.equals(TriggerType.START)) {
                    requestMap.put("Schedule", scheduleId);
                    requestMap.put("Style", styleId);
                } else {
                    requestMap.put("Schedule", "default");
                    requestMap.put("Style", "default");
                }
            }
            setAttributes(attributesUri, requestMap);
        }
    }

    @Override
    public void refresh(String deviceId) {
        UUID id = UUID.fromString(deviceId);
        Optional<TbDeviceCredentialsEntity> credentialsEntity = tbDeviceCredentialsService.findByDeviceId(id);
        if (credentialsEntity.isPresent() && credentialsEntity.get().getCredentialsType().equals("ACCESS_TOKEN")) {
            String accessToken = credentialsEntity.get().getCredentialsId();
            String attributesUri =
                    isSecure ? "https" : "http" + "://" + host + ":" + port + "/api/v1/" + accessToken + "/attributes";
            Map<String, String> requestMap = new HashMap<>();
            requestMap.put("Refresh", "true");
            setAttributes(attributesUri, requestMap);
            requestMap.put("Refresh", "false");
            setAttributes(attributesUri, requestMap);
        }
    }

    private void setAttributes(String attributesUri, Map<String, String> requestMap) {
        ResponseEntity<JsonNode> responseEntity;
        responseEntity = restTemplate.postForEntity(attributesUri, requestMap, JsonNode.class);
        log.trace("Set attributes response: {}", responseEntity.getStatusCode());
//        try {
//            JsonNode request = JsonUtils.mapToJsonNode(requestMap);
//        } catch (JsonProcessingException e) {
//            log.warn(e.getMessage());
//        }
    }

//    private boolean inSchedule(PeriodSignageScheduleEntity periodSchedule) {
//        LocalDate today = LocalDate.now();
//        LocalTime currentTime = LocalTime.now();
//        return !periodSchedule.getStartDate().isBefore(today) &&
//                !periodSchedule.getEndDate().isAfter(today) &&
//                !periodSchedule.getStartTime().isBefore(currentTime) &&
//                !periodSchedule.getEndTime().isAfter(currentTime);
//    }
}