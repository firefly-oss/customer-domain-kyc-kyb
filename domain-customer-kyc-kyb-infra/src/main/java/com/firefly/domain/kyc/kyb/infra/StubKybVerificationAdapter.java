package com.firefly.domain.kyc.kyb.infra;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

/**
 * Stub adapter for KYB verification that simulates a positive verification
 * with a 200ms delay.
 * <p>
 * TODO: Replace stub with real KYB provider (e.g., Sumsub, Onfido, Dow Jones)
 * </p>
 */
@Component
@ConditionalOnProperty(name = "integration.kyb-verification.provider", havingValue = "stub", matchIfMissing = true)
@Slf4j
public class StubKybVerificationAdapter implements KybVerificationPort {

    @Override
    public Mono<KybVerificationResult> verify(UUID caseId) {
        log.info("Stub KYB verification for caseId={}", caseId);
        return Mono.just(KybVerificationResult.verified())
                .delayElement(Duration.ofMillis(200));
    }
}
