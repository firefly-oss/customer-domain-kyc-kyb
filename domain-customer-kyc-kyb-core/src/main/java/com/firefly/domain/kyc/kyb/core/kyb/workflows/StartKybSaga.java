package com.firefly.domain.kyc.kyb.core.kyb.workflows;

import com.firefly.core.kycb.sdk.model.ComplianceCaseDTO;
import com.firefly.domain.kyc.kyb.core.kyb.commands.CreateKybCaseCommand;
import com.firefly.domain.kyc.kyb.core.kyb.commands.DeleteKybCaseCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.command.CommandBus;
import org.fireflyframework.orchestration.core.argument.SetVariable;
import org.fireflyframework.orchestration.core.context.ExecutionContext;
import org.fireflyframework.orchestration.saga.annotation.OnSagaComplete;
import org.fireflyframework.orchestration.saga.annotation.OnSagaError;
import org.fireflyframework.orchestration.saga.annotation.Saga;
import org.fireflyframework.orchestration.saga.annotation.SagaStep;
import org.fireflyframework.orchestration.saga.engine.SagaResult;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static com.firefly.domain.kyc.kyb.core.kyb.constants.KybConstants.*;

/**
 * Saga that creates a new KYB compliance case in the core layer.
 * <p>
 * Single-step saga with compensation to cancel the created case on failure.
 * Dispatches commands through the {@link CommandBus} to dedicated handlers.
 * </p>
 */
@Component
@Saga(name = SAGA_START_KYB)
@RequiredArgsConstructor
@Slf4j
public class StartKybSaga {

    private final CommandBus commandBus;

    /**
     * Creates a new compliance case of type KYB.
     *
     * @return the created compliance case DTO
     */
    @SagaStep(id = STEP_CREATE_CASE, compensate = "cancelCase", retry = 2, backoffMs = 1000)
    @SetVariable(CTX_CASE_ID)
    public Mono<ComplianceCaseDTO> createCase() {
        var cmd = CreateKybCaseCommand.builder().build();
        cmd.caseType("KYB");
        cmd.caseStatus("OPEN");

        log.info("Creating KYB compliance case");
        return commandBus.<ComplianceCaseDTO>send(cmd)
                .doOnNext(created -> log.info("KYB case created: caseId={}", created.getComplianceCaseId()));
    }

    /**
     * Compensation: cancels the created compliance case.
     */
    public Mono<Void> cancelCase(ComplianceCaseDTO createdCase) {
        if (createdCase == null || createdCase.getComplianceCaseId() == null) {
            return Mono.empty();
        }
        log.warn("Compensating: cancelling KYB case: caseId={}", createdCase.getComplianceCaseId());
        return commandBus.send(new DeleteKybCaseCommand(createdCase.getComplianceCaseId()));
    }

    @OnSagaComplete
    public void onComplete(ExecutionContext ctx, SagaResult result) {
        log.info("KYB case creation completed successfully");
    }

    @OnSagaError
    public void onError(Throwable error, ExecutionContext ctx) {
        log.error("KYB case creation failed: {}", error.getMessage());
    }
}
