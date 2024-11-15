package com.fgiotlead.ds.edge.config;

import io.rsocket.core.Resume;
import io.rsocket.metadata.WellKnownMimeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.rsocket.messaging.RSocketStrategiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.rsocket.metadata.SimpleAuthenticationEncoder;
import org.springframework.security.rsocket.metadata.UsernamePasswordMetadata;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@Configuration
public class RSocketConfiguration {

    @Value("${cloud.rpc.host}")
    private String cloudHost;
    @Value("${cloud.rsocket.port}")
    private int rSocketPort;
    @Value("${cloud.routingKey}")
    private String routingKey;
    @Value("${cloud.secret}")
    private String secret;
    @Value("${rsocket.connect.retry.interval}")
    private long retryInterval;
    @Value("${rsocket.connect.resume.interval}")
    private long resumeInterval;


    @Bean
    public RSocketStrategiesCustomizer rSocketStrategiesCustomizer() {
        return strategies -> strategies.encoder(new SimpleAuthenticationEncoder());
    }

    @Bean
    public RSocketRequester rSocketRequester(
            RSocketMessageHandler rSocketMessageHandler,
            RSocketRequester.Builder builder
    ) {
        UsernamePasswordMetadata usernamePasswordMetadata = new UsernamePasswordMetadata(routingKey, secret);
        MimeType mimeType = MimeTypeUtils.parseMimeType(WellKnownMimeType.MESSAGE_RSOCKET_AUTHENTICATION.getString());
        return builder
                .rsocketConnector(connector -> {
                    connector.acceptor(rSocketMessageHandler.responder());
                    connector.resume(resumeStrategy());
                    connector.reconnect(retryStrategy());
                })
                .setupMetadata(usernamePasswordMetadata, mimeType)
                .tcp(cloudHost, rSocketPort);
    }

    private Retry retryStrategy() {
        return Retry
                .fixedDelay(Long.MAX_VALUE, Duration.ofSeconds(retryInterval))
                .jitter(0.5)
                .doBeforeRetry(retrySignal -> log.info("Retrying connection: {}", retrySignal.totalRetriesInARow()));
    }

    private Resume resumeStrategy() {
        return new Resume()
                .retry(Retry
                        .fixedDelay(Long.MAX_VALUE, Duration.ofSeconds(resumeInterval))
                        .jitter(0.1)
                        .doBeforeRetry(
                                retrySignal -> log.info("Resuming connection: {}", retrySignal.totalRetriesInARow())
                        )
                );
    }
}
