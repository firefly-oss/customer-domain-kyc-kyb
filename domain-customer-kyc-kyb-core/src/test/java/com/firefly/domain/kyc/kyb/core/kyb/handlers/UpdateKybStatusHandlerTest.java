package com.firefly.domain.kyc.kyb.core.kyb.handlers;

import com.firefly.core.kycb.sdk.api.KybVerificationApi;
import com.firefly.core.kycb.sdk.model.KybVerificationDTO;
import com.firefly.domain.kyc.kyb.core.kyb.commands.VerifyKybCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateKybStatusHandlerTest {

    @Mock
    private KybVerificationApi kybVerificationApi;

    private UpdateKybStatusHandler handler;

    @BeforeEach
    void setUp() {
        handler = new UpdateKybStatusHandler(kybVerificationApi);
    }

    @Test
    void doHandle_updatesStatusToVerified_completesEmpty() {
        VerifyKybCommand cmd = VerifyKybCommand.builder()
                .caseId(UUID.randomUUID())
                .partyId(UUID.randomUUID())
                .build();

        KybVerificationDTO response = new KybVerificationDTO(null, null, cmd.getCaseId());
        when(kybVerificationApi.updateKybVerification(any(), any(), any(), anyString()))
                .thenReturn(Mono.just(response));

        StepVerifier.create(handler.doHandle(cmd))
                .verifyComplete();
    }

    @Test
    void doHandle_apiError_propagatesError() {
        VerifyKybCommand cmd = VerifyKybCommand.builder()
                .caseId(UUID.randomUUID())
                .partyId(UUID.randomUUID())
                .build();

        when(kybVerificationApi.updateKybVerification(any(), any(), any(), anyString()))
                .thenReturn(Mono.error(new RuntimeException("Update failed")));

        StepVerifier.create(handler.doHandle(cmd))
                .expectErrorMessage("Update failed")
                .verify();
    }
}
