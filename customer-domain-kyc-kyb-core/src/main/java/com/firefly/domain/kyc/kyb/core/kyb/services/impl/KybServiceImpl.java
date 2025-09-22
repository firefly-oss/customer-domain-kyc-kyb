package com.firefly.domain.kyc.kyb.core.kyb.services.impl;

import com.firefly.domain.kyc.kyb.core.kyb.commands.AttachEvidenceCommand;
import com.firefly.domain.kyc.kyb.core.kyb.services.KybService;
import com.firefly.transactional.core.SagaResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class KybServiceImpl implements KybService {
    
    @Override
    public Mono<SagaResult> startKyb() {
        return Mono.empty();
    }
    
    @Override
    public Mono<SagaResult> attachEvidence(UUID caseId, AttachEvidenceCommand command) {
        return Mono.empty();
    }
    
    @Override
    public Mono<SagaResult> markDocsComplete(UUID caseId) {
        return Mono.empty();
    }
    
    @Override
    public Mono<SagaResult> verifyKyb(UUID caseId) {
        return Mono.empty();
    }
    
    @Override
    public Mono<SagaResult> failKyb(UUID caseId) {
        return Mono.empty();
    }
    
    @Override
    public Mono<SagaResult> expireKyb(UUID caseId) {
        return Mono.empty();
    }
    
    @Override
    public Mono<SagaResult> renewKyb(UUID caseId) {
        return Mono.empty();
    }
}
