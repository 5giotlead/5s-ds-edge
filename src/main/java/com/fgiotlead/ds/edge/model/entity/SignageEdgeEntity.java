package com.fgiotlead.ds.edge.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fgiotlead.ds.edge.model.enumEntity.DownlinkStatus;
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
@Table(name = "signage_edge")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignageEdgeEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;
    private boolean enable;
    private DownlinkStatus status;

    @OneToMany(mappedBy = "edge", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    @JsonManagedReference
    private List<SignageProfileEntity> profiles = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SignageEdgeEntity that = (SignageEdgeEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
