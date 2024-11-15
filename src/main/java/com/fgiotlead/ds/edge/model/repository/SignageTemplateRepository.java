package com.fgiotlead.ds.edge.model.repository;

import com.fgiotlead.ds.edge.model.entity.SignageTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SignageTemplateRepository extends JpaRepository<SignageTemplateEntity, UUID> {
    Optional<SignageTemplateEntity> findByName(String name);
}
