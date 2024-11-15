package com.fgiotlead.ds.edge.model.repository;

import com.fgiotlead.ds.edge.model.entity.SignageProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SignageProfileRepository extends JpaRepository<SignageProfileEntity, UUID> {
}
