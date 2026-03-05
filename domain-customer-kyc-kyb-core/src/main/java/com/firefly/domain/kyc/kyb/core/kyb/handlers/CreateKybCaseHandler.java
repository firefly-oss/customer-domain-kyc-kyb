package com.firefly.domain.kyc.kyb.core.kyb.handlers;

import com.firefly.core.kycb.sdk.api.ComplianceCasesApi;
import com.firefly.core.kycb.sdk.model.ComplianceCaseDTO;
import com.firefly.domain.kyc.kyb.core.kyb.commands.CreateKybCaseCommand;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Handler that creates a KYB compliance case via the core SDK.
 */
@CommandHandlerComponent
public class CreateKybCaseHandler extends CommandHandler<CreateKybCaseCommand, ComplianceCaseDTO> {

    private final ComplianceCasesApi complianceCasesApi;

    public CreateKybCaseHandler(ComplianceCasesApi complianceCasesApi) {
        this.complianceCasesApi = complianceCasesApi;
    }

    @Override
    protected Mono<ComplianceCaseDTO> doHandle(CreateKybCaseCommand cmd) {
        return complianceCasesApi.createComplianceCase(cmd, UUID.randomUUID().toString());
    }
}
