package com.firefly.domain.kyc.kyb.core.kyb.services.impl;

import com.firefly.domain.kyc.kyb.core.kyb.commands.AttachEvidenceCommand;
import com.firefly.domain.kyc.kyb.core.kyb.commands.UpdateCaseStatusCommand;
import org.fireflyframework.cqrs.command.CommandBus;
import org.fireflyframework.orchestration.saga.engine.SagaEngine;
import org.fireflyframework.orchestration.saga.engine.SagaResult;
import org.fireflyframework.orchestration.saga.engine.StepInputs;
import org.fireflyframework.orchestration.saga.registry.SagaDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static com.firefly.domain.kyc.kyb.core.kyb.constants.KybConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KybServiceImplTest {

    @Mock
    private SagaEngine sagaEngine;

    @Mock
    private CommandBus commandBus;

    @Mock
    private SagaResult sagaResult;

    private KybServiceImpl kybService;

    @BeforeEach
    void setUp() {
        kybService = new KybServiceImpl(sagaEngine, commandBus);
    }

    @Test
    void startKyb_executesSaga() {
        when(sagaEngine.execute(eq(SAGA_START_KYB), any(StepInputs.class)))
                .thenReturn(Mono.just(sagaResult));
        when(sagaResult.isSuccess()).thenReturn(true);

        StepVerifier.create(kybService.startKyb())
                .assertNext(result -> assertThat(result.isSuccess()).isTrue())
                .verifyComplete();

        verify(sagaEngine).execute(eq(SAGA_START_KYB), any(StepInputs.class));
    }

    @Test
    void attachEvidence_executesSaga() {
        UUID caseId = UUID.randomUUID();
        AttachEvidenceCommand command = new AttachEvidenceCommand();
        command.setDocumentType("STATUTES");
        command.setDocumentName("company_statutes.pdf");

        when(sagaEngine.execute(eq(SAGA_ATTACH_EVIDENCE), any(StepInputs.class)))
                .thenReturn(Mono.just(sagaResult));
        when(sagaResult.isSuccess()).thenReturn(true);

        StepVerifier.create(kybService.attachEvidence(caseId, command))
                .assertNext(result -> assertThat(result.isSuccess()).isTrue())
                .verifyComplete();

        assertThat(command.getCaseId()).isEqualTo(caseId);
        verify(sagaEngine).execute(eq(SAGA_ATTACH_EVIDENCE), any(StepInputs.class));
    }

    @Test
    void verifyKyb_executesSaga() {
        UUID caseId = UUID.randomUUID();

        when(sagaEngine.execute(eq(SAGA_VERIFY_KYB), any(StepInputs.class)))
                .thenReturn(Mono.just(sagaResult));
        when(sagaResult.isSuccess()).thenReturn(true);

        StepVerifier.create(kybService.verifyKyb(caseId))
                .assertNext(result -> assertThat(result.isSuccess()).isTrue())
                .verifyComplete();

        verify(sagaEngine).execute(eq(SAGA_VERIFY_KYB), any(StepInputs.class));
    }

    @Test
    void markDocsComplete_dispatchesCommandViaSaga() {
        UUID caseId = UUID.randomUUID();

        when(sagaEngine.execute(any(SagaDefinition.class), any(StepInputs.class)))
                .thenReturn(Mono.just(sagaResult));
        when(sagaResult.isSuccess()).thenReturn(true);

        StepVerifier.create(kybService.markDocsComplete(caseId))
                .assertNext(result -> assertThat(result.isSuccess()).isTrue())
                .verifyComplete();

        verify(sagaEngine).execute(any(SagaDefinition.class), any(StepInputs.class));
    }

    @Test
    void failKyb_dispatchesCommandViaSaga() {
        UUID caseId = UUID.randomUUID();

        when(sagaEngine.execute(any(SagaDefinition.class), any(StepInputs.class)))
                .thenReturn(Mono.just(sagaResult));
        when(sagaResult.isSuccess()).thenReturn(true);

        StepVerifier.create(kybService.failKyb(caseId))
                .assertNext(result -> assertThat(result.isSuccess()).isTrue())
                .verifyComplete();
    }

    @Test
    void expireKyb_dispatchesCommandViaSaga() {
        UUID caseId = UUID.randomUUID();

        when(sagaEngine.execute(any(SagaDefinition.class), any(StepInputs.class)))
                .thenReturn(Mono.just(sagaResult));
        when(sagaResult.isSuccess()).thenReturn(true);

        StepVerifier.create(kybService.expireKyb(caseId))
                .assertNext(result -> assertThat(result.isSuccess()).isTrue())
                .verifyComplete();
    }

    @Test
    void renewKyb_dispatchesCommandViaSaga() {
        UUID caseId = UUID.randomUUID();

        when(sagaEngine.execute(any(SagaDefinition.class), any(StepInputs.class)))
                .thenReturn(Mono.just(sagaResult));
        when(sagaResult.isSuccess()).thenReturn(true);

        StepVerifier.create(kybService.renewKyb(caseId))
                .assertNext(result -> assertThat(result.isSuccess()).isTrue())
                .verifyComplete();
    }
}
