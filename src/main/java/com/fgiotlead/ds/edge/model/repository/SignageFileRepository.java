package com.fgiotlead.ds.edge.model.repository;

import com.fgiotlead.ds.edge.model.entity.SignageFileEntity;
import com.fgiotlead.ds.edge.model.enumEntity.DownlinkStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SignageFileRepository extends JpaRepository<SignageFileEntity, UUID> {
    @Modifying
    @Query("UPDATE SignageFileEntity " +
            "SET status = ?1 " +
            "WHERE id = ?2 " +
            "AND status <> ?1")
    int updateStatusById(DownlinkStatus downlinkStatus, UUID id);

    List<SignageFileEntity> deleteByBlocksIsNull();

    List<SignageFileEntity> findByStatus(DownlinkStatus status);

}
