package com.fgiotlead.ds.edge.model.service.Impl;

import com.fgiotlead.ds.edge.model.entity.SignageProfileEntity;
import com.fgiotlead.ds.edge.model.enumEntity.SignageType;
import com.fgiotlead.ds.edge.model.repository.SignageProfileRepository;
import com.fgiotlead.ds.edge.model.service.SignageProfileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@AllArgsConstructor
public class SignageProfileServiceImpl implements SignageProfileService {

    private SignageProfileRepository signageProfileRepository;

    @Override
    public List<SignageProfileEntity> findAll() {
        return signageProfileRepository.findAll();
    }

    @Override
    public Optional<SignageProfileEntity> findById(UUID id) {
        return signageProfileRepository.findById(id);
    }

    @Override
    public void saveAll(Map<SignageType, SignageProfileEntity> profiles) {
        signageProfileRepository.saveAll(profiles.values());
    }

    @Override
    public SignageProfileEntity save(SignageProfileEntity signageProfile) {
        return signageProfileRepository.save(signageProfile);
    }

    @Override
    public Map<String, String> delete(SignageProfileEntity signageProfile) {
        Map<String, String> responseEntity = new HashMap<>();
        signageProfileRepository.delete(signageProfile);
        return responseEntity;
    }
}
