package com.firefly.domain.kyc.kyb.core.kyb.handlers;

import com.firefly.core.kycb.sdk.api.KycVerificationDocumentsApi;
import com.firefly.core.kycb.sdk.model.VerificationDocumentDTO;
import com.firefly.domain.kyc.kyb.core.kyb.commands.AttachKybEvidenceCommand;
import lombok.RequiredArgsConstructor;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.firefly.domain.kyc.kyb.core.utils.constants.KycKybConstants.*;

/**
 * Handles attaching a verification document to an existing KYB case
 * via the kycb-mgmt SDK.
 */
@CommandHandlerComponent
@RequiredArgsConstructor
public class UploadKybDocumentHandler extends CommandHandler<AttachKybEvidenceCommand, UUID> {

    private final KycVerificationDocumentsApi kycVerificationDocumentsApi;

    /**
     * Uploads a verification document and links it to the KYB case.
     *
     * @param cmd the attach-evidence command containing document metadata
     * @return a Mono emitting the newly created document identifier
     */
    @Override
    protected Mono<UUID> doHandle(AttachKybEvidenceCommand cmd) {
        VerificationDocumentDTO docDto = new VerificationDocumentDTO()
                .kycVerificationId(cmd.getCaseId())
                .documentType(cmd.getDocumentType())
                .documentReference(cmd.getFingerprint())
                .verificationPurpose(PURPOSE_BUSINESS_VERIFICATION)
                .isVerified(false);

        return kycVerificationDocumentsApi.addVerificationDocument(
                        cmd.getPartyId(), cmd.getCaseId(), docDto, UUID.randomUUID().toString())
                .map(VerificationDocumentDTO::getVerificationDocumentId);
    }
}
