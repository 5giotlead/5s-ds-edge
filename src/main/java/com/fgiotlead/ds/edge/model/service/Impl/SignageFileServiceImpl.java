package com.fgiotlead.ds.edge.model.service.Impl;

import com.fgiotlead.ds.edge.model.entity.SignageFileEntity;
import com.fgiotlead.ds.edge.model.enumEntity.DownlinkStatus;
import com.fgiotlead.ds.edge.model.repository.SignageFileRepository;
import com.fgiotlead.ds.edge.model.service.SignageFileService;
import com.fgiotlead.ds.edge.rsocket.model.service.RSocketService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@Transactional(rollbackFor = IOException.class)
@AllArgsConstructor
public class SignageFileServiceImpl implements SignageFileService {

    private static final String ROOT_PATH = "resource/";

    private SignageFileRepository signageFileRepository;
    private RSocketService rSocketService;

    @Override
    public void save(SignageFileEntity file) {
        signageFileRepository.save(file);
    }

    @Override
    public void deleteById(UUID fileId) {
        Optional<SignageFileEntity> file = signageFileRepository.findById(fileId);
        if (file.isPresent()) {
            try {
                file.get().getBlocks().forEach(
                        signageBlock -> signageBlock.getFiles().remove(file.get())
                );
                signageFileRepository.deleteById(fileId);
            } catch (Exception e) {
                log.warn(e.getMessage());
            }
        }
    }

    @Override
    public List<SignageFileEntity> findAll() {
        return signageFileRepository.findAll();
    }

    @Override
    public List<SignageFileEntity> findByStatus(DownlinkStatus status) {
        return signageFileRepository.findByStatus(status);
    }

    @Override
    public void deleteByBlocksIsNull() {
        signageFileRepository.deleteByBlocksIsNull();
    }

    @Override
    public Flux<ByteBuffer> checkFiles(Set<SignageFileEntity> files) {
        List<SignageFileEntity> existFiles = signageFileRepository.findAll();
        Flux<ByteBuffer> mergedFlux = Flux.empty();
        for (SignageFileEntity file : files) {
            if (!existFiles.contains(file)) {
                mergedFlux = mergedFlux.mergeWith(this.saveStream(file));
            } else {
                log.info("file <{}.{}> existed, skip download.", file.getOriginalName(), file.getMimeType());
            }
        }
        return mergedFlux;
    }

    @Override
    public void checkUselessFiles() {
        List<SignageFileEntity> uselessFiles = signageFileRepository.deleteByBlocksIsNull();
        for (SignageFileEntity file : uselessFiles) {
            this.removeFile(file);
        }
    }

    private Flux<ByteBuffer> saveStream(SignageFileEntity file) {
        file.setStatus(DownlinkStatus.PENDING);
        Flux<ByteBuffer> flux = rSocketService.fileDownload(file.getId());
        String filePath = file.getAccess();
        String fileName = file.getId() + "." + file.getMimeType();
        Path path = Paths.get(ROOT_PATH + filePath + "/" + fileName);
        this.removeFile(file);
        AtomicReference<WritableByteChannel> wbc = new AtomicReference<>();
        return flux
                .doFirst(() -> this.onFirst(wbc, path))
                .doOnNext(byteBuffer -> this.receiveMessage(wbc.get(), byteBuffer))
                .doOnError(error -> this.receiveError(error, file))
                .doOnComplete(() -> this.onComplete(path, file))
                .doFinally(signalType -> this.doFinally(wbc.get()));
    }

    private void onFirst(AtomicReference<WritableByteChannel> wbc, Path path) {
        StandardOpenOption[] standardOpenOptions = {StandardOpenOption.CREATE, StandardOpenOption.APPEND};
        try {
            Files.createDirectories(path.getParent());
            wbc.set(Files.newByteChannel(path, standardOpenOptions));
        } catch (IOException e) {
            log.warn("onFirst - {}", String.valueOf(e.getCause()));
        }
    }

    private void receiveMessage(WritableByteChannel wbc, ByteBuffer byteBuffer) {
        try {
            wbc.write(byteBuffer);
        } catch (IOException e) {
            log.warn("receiveMessage - {}", String.valueOf(e.getCause()));
        }
    }

    private void receiveError(Throwable error, SignageFileEntity file) {
        file.setStatus(DownlinkStatus.ERROR);
        log.warn("receiveError - {}", String.valueOf(error.getCause()));
    }

    private void onComplete(Path path, SignageFileEntity file) {
        String hash = hash(path);
        if (!hash.isEmpty() && hash.equals(file.getHash())) {
            file.setStatus(DownlinkStatus.DEPLOYED);
            log.info("File <{}.{}> - Download Complete.", file.getOriginalName(), file.getMimeType());
        } else {
            file.setStatus(DownlinkStatus.ERROR);
            log.info("File <{}.{}> - Hash Not Match.", file.getOriginalName(), file.getMimeType());
            this.removeFile(file);
        }
        signageFileRepository.save(file);
    }

    private void doFinally(WritableByteChannel wbc) {
        try {
            wbc.close();
        } catch (IOException e) {
            log.warn("doFinally - {}", String.valueOf(e.getCause()));
        }
    }

    private void removeFile(SignageFileEntity file) {
        String filePath = file.getAccess();
        String fileName = file.getId() + "." + file.getMimeType();
        Path path = Paths.get(ROOT_PATH + filePath + "/" + fileName);
        if (Files.exists(path)) {
            try {
                Files.delete(path);
                log.info("File <{}.{}> - Removed.", file.getOriginalName(), file.getMimeType());
            } catch (IOException e) {
                log.warn(e.getMessage());
            }
        }
    }

    private String hash(Path path) {
        try {
            byte[] bytes = Files.readAllBytes(path);
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            byte[] hashBytes = messageDigest.digest(bytes);
            return new String(Hex.encode(hashBytes));
        } catch (NoSuchAlgorithmException | IOException e) {
            log.warn(e.getMessage());
        }
        return "";
    }
}
