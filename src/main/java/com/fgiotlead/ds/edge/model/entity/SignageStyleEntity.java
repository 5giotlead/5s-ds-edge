package com.fgiotlead.ds.edge.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fgiotlead.ds.edge.model.entity.schedule.RegularScheduleEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "signage_style")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignageStyleEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;
    private String name;
    private boolean editable;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(foreignKey = @ForeignKey(name = "TEMPLATE_ID_FK"))
    private SignageTemplateEntity template;

    @OneToMany(mappedBy = "style", cascade = CascadeType.ALL)
    @OrderBy("name")
//    @Schema(hidden = true)
    @Builder.Default
    @JsonManagedReference
    private List<SignageBlockEntity> blocks = new ArrayList<>();

    @OneToMany(mappedBy = "style", cascade = CascadeType.REFRESH)
//    @Schema(hidden = true)
    @Builder.Default
    @JsonIgnore
    private List<RegularScheduleEntity> schedules = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SignageStyleEntity that = (SignageStyleEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
