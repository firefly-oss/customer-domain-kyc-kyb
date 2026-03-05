package com.firefly.domain.kyc.kyb.web.controller;

import com.firefly.domain.kyc.kyb.core.kyb.commands.AttachEvidenceCommand;
import com.firefly.domain.kyc.kyb.core.kyb.services.KybService;
import com.firefly.domain.kyc.kyb.interfaces.dtos.kyb.KybCaseResponse;
import com.firefly.domain.kyc.kyb.interfaces.dtos.kyb.KybResultResponse;
import org.fireflyframework.orchestration.saga.engine.SagaResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KybControllerTest {

    @Mock
    private KybService kybService;

    @Mock
    private SagaResult sagaResult;

    private KybController controller;

    @BeforeEach
    void setUp() {
        controller = new KybController(kybService);
    }

    @Test
    void startKyb_returns201() {
        when(kybService.startKyb()).thenReturn(Mono.just(sagaResult));
        when(sagaResult.isSuccess()).thenReturn(true);

        StepVerifier.create(controller.startKyb())
                .assertNext(response -> {
                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
                    assertThat(response.getBody()).isNotNull();
                    assertThat(response.getBody().getStatus()).isEqualTo("SUCCESS");
                })
                .verifyComplete();
    }

    @Test
    void attachEvidence_returns200() {
        UUID caseId = UUID.randomUUID();
        AttachEvidenceCommand command = new AttachEvidenceCommand();
        command.setDocumentType("STATUTES");

        when(kybService.attachEvidence(eq(caseId), any())).thenReturn(Mono.just(sagaResult));
        when(sagaResult.isSuccess()).thenReturn(true);

        StepVerifier.create(controller.attachEvidence(caseId, command))
                .assertNext(response -> {
                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                    assertThat(response.getBody().getMessage()).isEqualTo("Evidence attached");
                })
                .verifyComplete();
    }

    @Test
    void verifyKyb_returnsResultResponse() {
        UUID caseId = UUID.randomUUID();

        when(kybService.verifyKyb(caseId)).thenReturn(Mono.just(sagaResult));
        when(sagaResult.isSuccess()).thenReturn(true);

        StepVerifier.create(controller.verifyKyb(caseId))
                .assertNext(response -> {
                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                    KybResultResponse body = response.getBody();
                    assertThat(body).isNotNull();
                    assertThat(body.getCaseId()).isEqualTo(caseId);
                    assertThat(body.isSuccess()).isTrue();
                })
                .verifyComplete();
    }

    @Test
    void failKyb_returns200() {
        UUID caseId = UUID.randomUUID();
        when(kybService.failKyb(caseId)).thenReturn(Mono.just(sagaResult));
        when(sagaResult.isSuccess()).thenReturn(true);

        StepVerifier.create(controller.failKyb(caseId))
                .assertNext(response -> {
                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                    assertThat(response.getBody().getMessage()).isEqualTo("KYB case failed");
                })
                .verifyComplete();
    }

    @Test
    void markDocsComplete_returns200() {
        UUID caseId = UUID.randomUUID();
        when(kybService.markDocsComplete(caseId)).thenReturn(Mono.just(sagaResult));
        when(sagaResult.isSuccess()).thenReturn(true);

        StepVerifier.create(controller.markDocsComplete(caseId))
                .assertNext(response -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK))
                .verifyComplete();
    }

    @Test
    void expireKyb_returns200() {
        UUID caseId = UUID.randomUUID();
        when(kybService.expireKyb(caseId)).thenReturn(Mono.just(sagaResult));
        when(sagaResult.isSuccess()).thenReturn(true);

        StepVerifier.create(controller.expireKyb(caseId))
                .assertNext(response -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK))
                .verifyComplete();
    }

    @Test
    void renewKyb_returns200() {
        UUID caseId = UUID.randomUUID();
        when(kybService.renewKyb(caseId)).thenReturn(Mono.just(sagaResult));
        when(sagaResult.isSuccess()).thenReturn(true);

        StepVerifier.create(controller.renewKyb(caseId))
                .assertNext(response -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK))
                .verifyComplete();
    }
}
