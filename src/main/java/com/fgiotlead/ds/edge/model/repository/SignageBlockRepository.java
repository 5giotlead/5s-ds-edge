package com.fgiotlead.ds.edge.model.repository;

import com.fgiotlead.ds.edge.model.entity.SignageBlockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SignageBlockRepository extends JpaRepository<SignageBlockEntity, UUID> {
}
