package com.firefly.domain.kyc.kyb.core.kyb.commands;

import com.firefly.domain.kyc.kyb.infra.KybVerificationResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fireflyframework.cqrs.command.Command;

import java.util.UUID;

/**
 * Command to request KYB verification from the external provider.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestKybVerificationCommand implements Command<KybVerificationResult> {

    private UUID caseId;
}
