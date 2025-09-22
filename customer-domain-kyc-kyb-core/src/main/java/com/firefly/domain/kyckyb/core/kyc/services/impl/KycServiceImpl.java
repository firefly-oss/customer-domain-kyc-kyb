package com.firefly.domain.kyckyb.core.kyc.services.impl;

import com.firefly.domain.kyckyb.core.common.SagaResult;
import com.firefly.domain.kyckyb.core.kyc.commands.AttachEvidenceCommand;
import com.firefly.domain.kyckyb.core.kyc.services.KycService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class KycServiceImpl implements KycService {
    
    @Override
    public Mono<SagaResult> openCase() {
        return Mono.just(SagaResult.success());
    }
    
    @Override
    public Mono<SagaResult> attachEvidence(UUID caseId, AttachEvidenceCommand command) {
        return Mono.just(SagaResult.success());
    }
    
    @Override
    public Mono<SagaResult> markDocsComplete(UUID caseId) {
        return Mono.just(SagaResult.success());
    }
    
    @Override
    public Mono<SagaResult> recordPepAdverseMedia(UUID caseId) {
        return Mono.just(SagaResult.success());
    }
    
    @Override
    public Mono<SagaResult> verify(UUID caseId) {
        return Mono.just(SagaResult.success());
    }
    
    @Override
    public Mono<SagaResult> fail(UUID caseId) {
        return Mono.just(SagaResult.success());
    }
    
    @Override
    public Mono<SagaResult> expire(UUID caseId) {
        return Mono.just(SagaResult.success());
    }
    
    @Override
    public Mono<SagaResult> renew(UUID caseId) {
        return Mono.just(SagaResult.success());
    }
}
