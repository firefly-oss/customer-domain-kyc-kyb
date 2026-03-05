package com.firefly.domain.kyc.kyb.core.kyb.handlers;

import com.firefly.core.kycb.sdk.api.ComplianceCasesApi;
import com.firefly.domain.kyc.kyb.core.kyb.commands.DeleteKybCaseCommand;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import reactor.core.publisher.Mono;

/**
 * Handler that deletes (cancels) a KYB compliance case via the core SDK.
 */
@CommandHandlerComponent
public class DeleteKybCaseHandler extends CommandHandler<DeleteKybCaseCommand, Void> {

    private final ComplianceCasesApi complianceCasesApi;

    public DeleteKybCaseHandler(ComplianceCasesApi complianceCasesApi) {
        this.complianceCasesApi = complianceCasesApi;
    }

    @Override
    protected Mono<Void> doHandle(DeleteKybCaseCommand cmd) {
        return complianceCasesApi.deleteComplianceCase(cmd.getCaseId()).then();
    }
}
