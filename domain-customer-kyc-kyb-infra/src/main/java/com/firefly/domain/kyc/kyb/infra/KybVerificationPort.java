package com.firefly.domain.kyc.kyb.infra;

import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Port for external KYB verification providers.
 * <p>
 * Abstracts the actual third-party provider used for business entity verification.
 * </p>
 */
public interface KybVerificationPort {

    /**
     * Initiates a KYB verification for the given compliance case.
     *
     * @param caseId the compliance case identifier
     * @return a {@link KybVerificationResult} with the verification outcome
     */
    Mono<KybVerificationResult> verify(UUID caseId);
}
