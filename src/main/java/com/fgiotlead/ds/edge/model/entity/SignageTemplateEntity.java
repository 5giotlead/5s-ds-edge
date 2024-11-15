package com.fgiotlead.ds.edge.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

@Entity
@DynamicUpdate
@Table(name = "signage_template")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignageTemplateEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;
    private String name;
    private HashMap<String, String> blocksType;

    @OneToMany(mappedBy = "template", cascade = CascadeType.REFRESH)
//    @Schema(hidden = true)
    @Builder.Default
    @JsonIgnore
    private List<SignageStyleEntity> styles = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SignageTemplateEntity that = (SignageTemplateEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
