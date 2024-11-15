package com.fgiotlead.ds.edge.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "signage_block")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignageBlockEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;
    private String name;
    private Integer interval;
    private ArrayList<String> text;

    @ManyToMany(cascade = {CascadeType.REFRESH})
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "signage_block_file",
            joinColumns = @JoinColumn(foreignKey = @ForeignKey(name = "BLOCK_ID_FK")),
            inverseJoinColumns = @JoinColumn(foreignKey = @ForeignKey(name = "FILE_ID_FK")))
    @Builder.Default
    private List<SignageFileEntity> files = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(foreignKey = @ForeignKey(name = "STYLE_ID_FK"))
    @JsonBackReference
    private SignageStyleEntity style;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SignageBlockEntity that = (SignageBlockEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
