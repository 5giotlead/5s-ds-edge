package com.fgiotlead.ds.edge.model.entity.schedule;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fgiotlead.ds.edge.model.entity.SignageProfileEntity;
import com.fgiotlead.ds.edge.model.entity.SignageStyleEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
public abstract class SignageScheduleEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;
    private LocalTime startTime;
    private LocalTime endTime;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(foreignKey = @ForeignKey(name = "STYLE_ID_FK"))
    private SignageStyleEntity style;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "PROFILE_ID_FK"))
    @JsonBackReference
    private SignageProfileEntity profile;

}