package com.firefly.domain.kyc.kyb.core.kyb.handlers;

import com.firefly.core.kycb.sdk.api.CorporateDocumentsApi;
import com.firefly.domain.kyc.kyb.core.kyb.commands.DeleteCorporateDocumentCommand;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Handler that deletes a corporate document via the core SDK (compensation).
 */
@CommandHandlerComponent
public class DeleteCorporateDocumentHandler extends CommandHandler<DeleteCorporateDocumentCommand, Void> {

    private final CorporateDocumentsApi corporateDocumentsApi;

    public DeleteCorporateDocumentHandler(CorporateDocumentsApi corporateDocumentsApi) {
        this.corporateDocumentsApi = corporateDocumentsApi;
    }

    @Override
    protected Mono<Void> doHandle(DeleteCorporateDocumentCommand cmd) {
        return corporateDocumentsApi.deleteCorporateDocument(cmd.getDocumentId(), UUID.randomUUID().toString()).then();
    }
}
