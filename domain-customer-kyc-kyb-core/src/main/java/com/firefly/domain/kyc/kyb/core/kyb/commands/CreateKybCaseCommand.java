package com.firefly.domain.kyc.kyb.core.kyb.commands;

import com.firefly.core.kycb.sdk.model.ComplianceCaseDTO;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fireflyframework.cqrs.command.Command;

/**
 * Command to create a new KYB compliance case in the core layer.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class CreateKybCaseCommand extends ComplianceCaseDTO implements Command<ComplianceCaseDTO> {
}
