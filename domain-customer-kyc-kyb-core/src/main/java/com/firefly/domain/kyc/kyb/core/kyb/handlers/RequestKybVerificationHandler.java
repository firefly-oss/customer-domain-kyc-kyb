package com.firefly.domain.kyc.kyb.core.kyb.handlers;

import com.firefly.domain.kyc.kyb.core.kyb.commands.RequestKybVerificationCommand;
import com.firefly.domain.kyc.kyb.infra.KybVerificationPort;
import com.firefly.domain.kyc.kyb.infra.KybVerificationResult;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import reactor.core.publisher.Mono;

/**
 * Handler that requests KYB verification from the external provider.
 */
@CommandHandlerComponent
public class RequestKybVerificationHandler extends CommandHandler<RequestKybVerificationCommand, KybVerificationResult> {

    private final KybVerificationPort kybVerificationPort;

    public RequestKybVerificationHandler(KybVerificationPort kybVerificationPort) {
        this.kybVerificationPort = kybVerificationPort;
    }

    @Override
    protected Mono<KybVerificationResult> doHandle(RequestKybVerificationCommand cmd) {
        return kybVerificationPort.verify(cmd.getCaseId());
    }
}
