package com.fgiotlead.ds.edge.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fgiotlead.ds.edge.model.enumEntity.DownlinkStatus;
import com.fgiotlead.ds.edge.model.listener.SignageFileListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@EntityListeners(SignageFileListener.class)
@Table(name = "signage_file")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignageFileEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;
    private String originalName;
    private String mimeType;
    private String access;
    private String hash;
    @Builder.Default
    private DownlinkStatus status = DownlinkStatus.DEPLOYED;

    @ManyToMany(mappedBy = "files", cascade = CascadeType.REFRESH)
    @Builder.Default
    @JsonIgnore
    private List<SignageBlockEntity> blocks = new ArrayList<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        SignageFileEntity file = (SignageFileEntity) o;
        return getId() != null && Objects.equals(getId(), file.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
