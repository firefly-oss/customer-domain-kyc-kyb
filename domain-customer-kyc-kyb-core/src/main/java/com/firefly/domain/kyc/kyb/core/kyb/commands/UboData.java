package com.firefly.domain.kyc.kyb.core.kyb.commands;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Value object carrying Ultimate Beneficial Owner data into the KYB workflow.
 * Passed inside {@link RegisterUbosCommand} and mapped to {@code UboDTO}
 * by {@code KybMapper} before the SDK call.
 */
@Data
@Builder
public class UboData {

    private UUID naturalPersonId;
    private BigDecimal ownershipPercentage;
    private String ownershipType;
    private String controlStructure;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String verificationMethod;
    private String titularidadRealDocument;
}
