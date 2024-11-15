package com.fgiotlead.ds.edge.quartz.model.service.Impl;//package com.fgiotlead.ds_edge.quartz.model.service.Impl;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fgiotlead.ds_edge.model.entity.schedule.RegularScheduleEntity;
//import com.fgiotlead.ds_edge.model.entity.schedule.SignageScheduleEntity;
//import com.fgiotlead.ds_edge.model.repository.schedule.SignageScheduleRepository;
//import com.fgiotlead.ds_edge.quartz.model.enumEntity.TriggerType;
//import com.fgiotlead.ds_edge.quartz.model.service.SignageJobService;
//import com.fgiotlead.ds_edge.tb.model.entity.TbDeviceCredentialsEntity;
//import com.fgiotlead.ds_edge.tb.model.service.TbDeviceCredentialsService;
//import com.fgiotlead.ds_edge.util.JsonUtils;
//import com.fgiotlead.ds_edge.util.TimeUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import jakarta.transaction.Transactional;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Service
//@Transactional
//public class SignageJobServiceImplMixed implements SignageJobService {
//
//    @Value("${server.address}")
//    private String host;
//    @Value("${server.port}")
//    private String port;
//    @Value("${server.ssl.enabled}")
//    private boolean isSecure;
//
//    @Autowired
//    private SignageScheduleRepository signageScheduleRepository;
//    @Autowired
//    private TbDeviceCredentialsService tbDeviceCredentialsService;
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @Override
//    public void checkSchedule(String deviceId, String scheduleId, String styleId, TriggerType type, String action) {
////        Optional<SignageScheduleEntity> schedule = signageScheduleRepository.findById(UUID.fromString(scheduleId));
////        if (schedule.isPresent()) {
////            boolean isPeriod = schedule.get() instanceof PeriodSignageScheduleEntity;
////            switch (action) {
////                case "job":
////                case "save":
////                    if (type.equals("start") &&
////                            TimeUtils.inSchedule(schedule.get()) &&
//////                            Transaction 原因，暫時無法直接查詢，會報錯，待解。
//////                            (isPeriod || periodScheduleRepository.findInSchedule(
//////                                    schedule.get().getGroup(),
//////                                    today,
//////                                    currentTime
//////                            ).isEmpty())
////                            (isPeriod ||
////                                    schedule.get().getProfiles().get()
////                                            .stream().noneMatch(this::inSchedule)
////                            )
////                    ) {
////                        changeStyle(deviceId, scheduleId, styleId, type);
////                    } else if (type.equals("end") && !TimeUtils.inSchedule(schedule.get())
////                    ) {
////                        List<NormalSignageScheduleEntity> normalSchedules = schedule.get().getSignageProfile().getNormalSchedules()
////                                .stream().filter(TimeUtils::inSchedule).collect(Collectors.toList());
////                        if (!normalSchedules.isEmpty()) {
////                            resumeSchedule(deviceId, normalSchedules.get(0));
////                        } else {
////                            changeStyle(deviceId, scheduleId, styleId, type);
////                        }
////                    }
////                    break;
////                case "toggle":
////                    if (TimeUtils.inSchedule(schedule.get())) {
////                        changeStyle(deviceId, scheduleId, styleId, type);
////                    }
////                    break;
////            }
////        }
//    }
//
//    @Override
//    public void deleteSchedule(UUID deviceId, SignageScheduleEntity schedule, TriggerType type) {
//        if (type.equals(TriggerType.END) && TimeUtils.inSchedule(schedule)) {
//            List<RegularScheduleEntity> schedules =
//                    schedule.getProfile().getSchedules()
//                            .stream().filter(TimeUtils::inSchedule).collect(Collectors.toList());
//            if (schedules.isEmpty()) {
//                changeStyle(
//                        deviceId.toString(),
//                        schedule.getId().toString(),
//                        schedule.getStyle().getId().toString(),
//                        type);
//            }
//        }
//    }
//
//    @Override
//    public void setToDefault(UUID deviceId) {
//        Optional<TbDeviceCredentialsEntity> credentialsEntity = tbDeviceCredentialsService.findByDeviceId(deviceId);
//        if (credentialsEntity.isPresent()) {
//            TbDeviceCredentialsEntity credentials = credentialsEntity.get();
//            if (credentials.getCredentialsId().equals("ACCESS_TOKEN")) {
//                String accessToken = credentialsEntity.get().getCredentialsId();
//                String attributesUri =
//                        isSecure ? "https" : "http" + "://" + host + "/api/v1/" + accessToken + "/attributes";
//                JsonNode jsonNode = restTemplate
//                        .getForObject(attributesUri + "?sharedKeys=Schedule,Style", JsonNode.class);
//            }
//        }
//    }
//
//    @Override
//    public void changeStyle(String deviceId, String scheduleId, String styleId, TriggerType type) {
//        UUID id = UUID.fromString(deviceId);
//        Optional<TbDeviceCredentialsEntity> credentialsEntity = tbDeviceCredentialsService.findByDeviceId(id);
//        if (credentialsEntity.isPresent()) {
//            TbDeviceCredentialsEntity credentials = credentialsEntity.get();
//            if (credentials.getCredentialsId().equals("ACCESS_TOKEN")) {
//                String accessToken = credentialsEntity.get().getCredentialsId();
//                String attributesUri =
//                        isSecure ? "https" : "http" + "://" + host + "/api/v1/" + accessToken + "/attributes";
//                JsonNode jsonNode = restTemplate
//                        .getForObject(attributesUri + "?sharedKeys=Schedule,Style", JsonNode.class);
//                if (jsonNode != null && jsonNode.get("shared") != null) {
//                    String currentScheduleId = "";
//                    String currentStyleId = "";
//                    if (!jsonNode.get("Schedule").isEmpty()) {
//                        currentScheduleId = jsonNode.get("Schedule").toString();
//                    }
//                    if (type.equals(TriggerType.START)) {
//                        if (!scheduleId.equals(currentScheduleId) || !styleId.equals(currentStyleId)) {
//                            setAttribute(deviceId, scheduleId, styleId);
//                        }
//                    } else {
//                        if (scheduleId.equals(currentScheduleId) && styleId.equals(currentStyleId)) {
////            if (!Objects.equals("default", currentScheduleId) && !Objects.equals("default", currentStyleId)) {
//                            setAttribute(deviceId, "default", "default");
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private void setAttribute(String deviceId, String scheduleId, String styleId) {
//        Map<String, String> requestMap = new HashMap<>();
//        requestMap.put("Schedule", scheduleId);
//        requestMap.put("Style", styleId);
//        try {
//            JsonNode request = JsonUtils.mapToJsonNode(requestMap);
////            restClient.saveDeviceAttributes(entityId, "SERVER_SCOPE", request);
//        } catch (JsonProcessingException e) {
//            log.warn(e.getMessage());
//        }
//    }
//
////    private boolean inSchedule(PeriodSignageScheduleEntity periodSchedule) {
////        LocalDate today = LocalDate.now();
////        LocalTime currentTime = LocalTime.now();
////        return !periodSchedule.getStartDate().isBefore(today) &&
////                !periodSchedule.getEndDate().isAfter(today) &&
////                !periodSchedule.getStartTime().isBefore(currentTime) &&
////                !periodSchedule.getEndTime().isAfter(currentTime);
////    }
//}