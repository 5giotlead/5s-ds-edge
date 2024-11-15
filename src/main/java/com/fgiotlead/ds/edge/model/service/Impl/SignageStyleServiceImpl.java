package com.fgiotlead.ds.edge.model.service.Impl;


import com.fgiotlead.ds.edge.model.entity.SignageStyleEntity;
import com.fgiotlead.ds.edge.model.repository.SignageStyleRepository;
import com.fgiotlead.ds.edge.model.service.SignageStyleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
@AllArgsConstructor
public class SignageStyleServiceImpl implements SignageStyleService {

    SignageStyleRepository signageStyleRepository;

    @Override
    public List<SignageStyleEntity> findAll() {
        return signageStyleRepository.findAll();
    }

    @Override
    public Optional<SignageStyleEntity> findById(UUID id) {
        return signageStyleRepository.findById(id);
    }

    @Override
    public Optional<SignageStyleEntity> findByName(String name) {
        return signageStyleRepository.findByName(name);
    }

    @Override
    public void saveAll(Set<SignageStyleEntity> styles) {
        signageStyleRepository.saveAll(styles);
    }

    @Override
    public SignageStyleEntity save(SignageStyleEntity style) {
        return signageStyleRepository.save(style);
    }

    @Override
    public void delete(SignageStyleEntity style) {
        signageStyleRepository.delete(style);
    }

    @Override
    public void checkUselessStyles() {
        signageStyleRepository.deleteBySchedulesIsNull();
    }
}
