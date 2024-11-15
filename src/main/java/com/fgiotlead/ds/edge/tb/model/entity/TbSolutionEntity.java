package com.fgiotlead.ds.edge.tb.model.entity;//package com.fgiotlead.ds_edge.tb.model.entity;
//
//import lombok.*;
//
//import jakarta.persistence.*;
//import java.io.Serializable;
//import java.util.List;
//import java.util.UUID;
//
//@Entity
//@Table(name = "solution")
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class TbSolutionEntity implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private UUID id;
//    private UUID tenantId;
//
//    @OneToMany
//    private List<TbDashboardEntity> dashboards;
//    @OneToMany
//    private List<TbWidgetEntity> widgets;
//    @OneToMany
//    private List<TbDeviceEntity> devices;
//}
