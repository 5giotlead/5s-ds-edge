package com.fgiotlead.ds.edge.model.repository;

import com.fgiotlead.ds.edge.model.entity.SignageStyleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SignageStyleRepository extends JpaRepository<SignageStyleEntity, UUID> {
    Optional<SignageStyleEntity> findByName(String name);

    List<SignageStyleEntity> deleteBySchedulesIsNull();
}
