package com.firefly.domain.kyckyb.core.kyb.services.impl;

import com.firefly.domain.kyckyb.core.common.SagaResult;
import com.firefly.domain.kyckyb.core.kyb.commands.AttachEvidenceCommand;
import com.firefly.domain.kyckyb.core.kyb.services.KybService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class KybServiceImpl implements KybService {
    
    @Override
    public Mono<SagaResult> startKyb() {
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
    public Mono<SagaResult> verifyKyb(UUID caseId) {
        return Mono.just(SagaResult.success());
    }
    
    @Override
    public Mono<SagaResult> failKyb(UUID caseId) {
        return Mono.just(SagaResult.success());
    }
    
    @Override
    public Mono<SagaResult> expireKyb(UUID caseId) {
        return Mono.just(SagaResult.success());
    }
    
    @Override
    public Mono<SagaResult> renewKyb(UUID caseId) {
        return Mono.just(SagaResult.success());
    }
}
