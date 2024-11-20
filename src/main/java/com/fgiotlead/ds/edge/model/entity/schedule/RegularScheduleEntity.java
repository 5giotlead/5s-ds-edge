package com.fgiotlead.ds.edge.model.entity.schedule;

import com.fgiotlead.ds.edge.model.listener.RegularScheduleListener;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@EntityListeners(RegularScheduleListener.class)
@Table(name = "signage_regular_schedule")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RegularScheduleEntity extends SignageScheduleEntity {

    private Integer weekDay;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RegularScheduleEntity that = (RegularScheduleEntity) o;
        return this.getId() != null && Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}