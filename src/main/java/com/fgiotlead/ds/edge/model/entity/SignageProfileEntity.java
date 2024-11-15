package com.fgiotlead.ds.edge.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fgiotlead.ds.edge.model.entity.schedule.RegularScheduleEntity;
import com.fgiotlead.ds.edge.model.enumEntity.SignageType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "signage_profile")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignageProfileEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;
    private SignageType type;
//    private HashMap<String, String> lastPlayInfo;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "EDGE_ID_FK"))
    @JsonBackReference
    private SignageEdgeEntity edge;

    @OneToMany(mappedBy = "profile", orphanRemoval = true, cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    @JsonManagedReference
    private List<RegularScheduleEntity> schedules = new ArrayList<>();


}
