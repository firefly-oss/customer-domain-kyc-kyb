package com.firefly.domain.kyc.kyb.core.kyb.handlers;

import com.firefly.core.kycb.sdk.api.KybVerificationApi;
import com.firefly.core.kycb.sdk.model.KybVerificationDTO;
import com.firefly.domain.kyc.kyb.core.kyb.queries.GetKybResultQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetKybResultHandlerTest {

    @Mock
    private KybVerificationApi kybVerificationApi;

    private GetKybResultHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GetKybResultHandler(kybVerificationApi);
    }

    @Test
    void shouldReturnVerificationDtoFromApi() {
        UUID partyId = UUID.randomUUID();
        UUID verificationId = UUID.randomUUID();

        KybVerificationDTO expectedDto = new KybVerificationDTO(null, null, verificationId);
        expectedDto.verificationStatus("VERIFIED");
        when(kybVerificationApi.getKybVerification(partyId, verificationId))
                .thenReturn(Mono.just(expectedDto));

        GetKybResultQuery query = GetKybResultQuery.builder()
                .partyId(partyId)
                .verificationId(verificationId)
                .build();

        StepVerifier.create(handler.doHandle(query))
                .assertNext(dto -> {
                    assertThat(dto.getKybVerificationId()).isEqualTo(verificationId);
                    assertThat(dto.getVerificationStatus()).isEqualTo("VERIFIED");
                })
                .verifyComplete();

        verify(kybVerificationApi).getKybVerification(partyId, verificationId);
    }

    @Test
    void shouldPropagateErrorWhenVerificationNotFound() {
        UUID partyId = UUID.randomUUID();
        UUID verificationId = UUID.randomUUID();

        when(kybVerificationApi.getKybVerification(partyId, verificationId))
                .thenReturn(Mono.error(new RuntimeException("Verification not found")));

        GetKybResultQuery query = GetKybResultQuery.builder()
                .partyId(partyId)
                .verificationId(verificationId)
                .build();

        StepVerifier.create(handler.doHandle(query))
                .expectError(RuntimeException.class)
                .verify();
    }
}
