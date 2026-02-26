package com.firefly.domain.kyc.kyb.core.kyb.services;

import com.firefly.domain.kyc.kyb.core.kyb.commands.AttachEvidenceCommand;
import org.fireflyframework.orchestration.saga.engine.SagaResult;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface KybService {
    
    /**
     * Opens a new KYB process for an organization.
     * @return Mono containing the result of starting the KYB process
     */
    Mono<SagaResult> startKyb();
    
    /**
     * Attaches statutes, registries, shareholder records, and POA documents to a KYB case.
     * @param caseId the unique identifier of the KYB case
     * @param command the command containing document information to attach
     * @return Mono containing the result of attaching evidence
     */
    Mono<SagaResult> attachEvidence(UUID caseId, AttachEvidenceCommand command);
    
    /**
     * Marks the KYB dossier as complete.
     * @param caseId the unique identifier of the KYB case
     * @return Mono containing the result of marking documents complete
     */
    Mono<SagaResult> markDocsComplete(UUID caseId);
    
    /**
     * Verifies entity (requires UBOs validated) and sets expiry.
     * @param caseId the unique identifier of the KYB case
     * @return Mono containing the result of KYB verification
     */
    Mono<SagaResult> verifyKyb(UUID caseId);
    
    /**
     * Rejects KYB due to opaque structure/sanctions/insufficient docs.
     * @param caseId the unique identifier of the KYB case
     * @return Mono containing the result of failing the KYB case
     */
    Mono<SagaResult> failKyb(UUID caseId);
    
    /**
     * Marks KYB as expired by time.
     * @param caseId the unique identifier of the KYB case
     * @return Mono containing the result of expiring the KYB case
     */
    Mono<SagaResult> expireKyb(UUID caseId);
    
    /**
     * Renews KYB level/expiry.
     * @param caseId the unique identifier of the KYB case
     * @return Mono containing the result of renewing the KYB case
     */
    Mono<SagaResult> renewKyb(UUID caseId);
}
