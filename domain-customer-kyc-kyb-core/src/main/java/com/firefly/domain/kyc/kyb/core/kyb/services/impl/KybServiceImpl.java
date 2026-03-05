package com.firefly.domain.kyc.kyb.core.kyb.services.impl;

import com.firefly.domain.kyc.kyb.core.kyb.commands.AttachEvidenceCommand;
import com.firefly.domain.kyc.kyb.core.kyb.commands.UpdateCaseStatusCommand;
import com.firefly.domain.kyc.kyb.core.kyb.services.KybService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.command.CommandBus;
import org.fireflyframework.orchestration.saga.builder.SagaBuilder;
import org.fireflyframework.orchestration.saga.engine.SagaEngine;
import org.fireflyframework.orchestration.saga.engine.SagaResult;
import org.fireflyframework.orchestration.saga.engine.StepInputs;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.firefly.domain.kyc.kyb.core.kyb.constants.KybConstants.*;

/**
 * Orchestrates KYB lifecycle operations using sagas for multi-step processes
 * and builder-defined sagas for single-step status transitions.
 * <p>
 * All core SDK calls are dispatched through the {@link CommandBus} to
 * dedicated {@link org.fireflyframework.cqrs.command.CommandHandler} implementations.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KybServiceImpl implements KybService {

    private final SagaEngine sagaEngine;
    private final CommandBus commandBus;

    @Override
    public Mono<SagaResult> startKyb() {
        log.info("Starting KYB process");
        return sagaEngine.execute(SAGA_START_KYB, StepInputs.empty());
    }

    @Override
    public Mono<SagaResult> attachEvidence(UUID caseId, AttachEvidenceCommand command) {
        log.info("Attaching KYB evidence: caseId={}", caseId);
        command.setCaseId(caseId);
        StepInputs inputs = StepInputs.of(STEP_ATTACH_DOCUMENT, command);
        return sagaEngine.execute(SAGA_ATTACH_EVIDENCE, inputs);
    }

    @Override
    public Mono<SagaResult> markDocsComplete(UUID caseId) {
        log.info("Marking KYB docs complete: caseId={}", caseId);
        var saga = SagaBuilder.saga("mark-docs-complete")
                .step("update-status")
                    .handler(() -> commandBus.send(new UpdateCaseStatusCommand(caseId, "DOCS_COMPLETE")))
                    .add()
                .build();
        return sagaEngine.execute(saga, StepInputs.empty());
    }

    @Override
    public Mono<SagaResult> verifyKyb(UUID caseId) {
        log.info("Verifying KYB: caseId={}", caseId);
        StepInputs inputs = StepInputs.of(STEP_REQUEST_VERIFICATION, caseId);
        return sagaEngine.execute(SAGA_VERIFY_KYB, inputs);
    }

    @Override
    public Mono<SagaResult> failKyb(UUID caseId) {
        log.info("Failing KYB: caseId={}", caseId);
        var saga = SagaBuilder.saga("fail-kyb")
                .step("update-status")
                    .handler(() -> commandBus.send(new UpdateCaseStatusCommand(caseId, "FAILED")))
                    .add()
                .build();
        return sagaEngine.execute(saga, StepInputs.empty());
    }

    @Override
    public Mono<SagaResult> expireKyb(UUID caseId) {
        log.info("Expiring KYB: caseId={}", caseId);
        var saga = SagaBuilder.saga("expire-kyb")
                .step("update-status")
                    .handler(() -> commandBus.send(new UpdateCaseStatusCommand(caseId, "EXPIRED")))
                    .add()
                .build();
        return sagaEngine.execute(saga, StepInputs.empty());
    }

    @Override
    public Mono<SagaResult> renewKyb(UUID caseId) {
        log.info("Renewing KYB: caseId={}", caseId);
        var saga = SagaBuilder.saga("renew-kyb")
                .step("update-status")
                    .handler(() -> commandBus.send(new UpdateCaseStatusCommand(caseId, "OPEN")))
                    .add()
                .build();
        return sagaEngine.execute(saga, StepInputs.empty());
    }
}
