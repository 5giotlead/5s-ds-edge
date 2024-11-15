package com.fgiotlead.ds.edge.model.listener;//package com.fgiotlead.ds_edge.model.listener;
//
//import com.fgiotlead.ds_edge.event.SignageEventPublisher;
//import com.fgiotlead.ds_edge.model.entity.SignageEdgeEntity;
//import lombok.AllArgsConstructor;
//
//import jakarta.persistence.PostPersist;
//import jakarta.persistence.PostUpdate;
//
//@AllArgsConstructor
//public class SignageEdgeListener {
//
//    SignageEventPublisher eventPublisher;
//
//    @PostPersist
//    public void afterPersist(SignageEdgeEntity edge) {
//        eventPublisher.publishStatusEvent(this, edge.getStatus());
//    }
//
//    @PostUpdate
//    public void afterUpdate(SignageEdgeEntity edge) {
//        eventPublisher.publishStatusEvent(this, edge.getStatus());
//    }
//}
