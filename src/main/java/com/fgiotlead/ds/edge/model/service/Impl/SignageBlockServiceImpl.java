package com.fgiotlead.ds.edge.model.service.Impl;


import com.fgiotlead.ds.edge.model.entity.SignageBlockEntity;
import com.fgiotlead.ds.edge.model.repository.SignageBlockRepository;
import com.fgiotlead.ds.edge.model.service.SignageBlockService;
import com.fgiotlead.ds.edge.model.service.SignageFileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
@AllArgsConstructor
public class SignageBlockServiceImpl implements SignageBlockService {

    SignageBlockRepository signageBlockRepository;
    SignageFileService signageFileService;

    @Override
    public List<SignageBlockEntity> findAll() {
        return signageBlockRepository.findAll();
    }

    @Override
    public Optional<SignageBlockEntity> findById(UUID id) {
        return signageBlockRepository.findById(id);
    }

    @Override
    public void save(SignageBlockEntity signageBlock) {
        signageBlockRepository.save(signageBlock);
    }

    @Override
    public void delete(SignageBlockEntity signageBlock) {
        signageBlockRepository.delete(signageBlock);
    }

    @Override
    public void persist(SignageBlockEntity signageBlock) {
    }

    @Override
    public void update(SignageBlockEntity signageBlock) {
    }

    @Override
    public void remove(SignageBlockEntity signageBlock) {

    }
}
