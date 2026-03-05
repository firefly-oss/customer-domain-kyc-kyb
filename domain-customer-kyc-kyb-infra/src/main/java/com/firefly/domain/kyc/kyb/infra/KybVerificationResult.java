package com.firefly.domain.kyc.kyb.infra;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Result of a KYB verification request from an external provider.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KybVerificationResult {

    /** Verification status: VERIFIED, REJECTED, PENDING. */
    private String status;

    /** Optional reason for rejection. */
    private String rejectionReason;

    /** Risk score from provider (0-100). */
    private Integer riskScore;

    public static KybVerificationResult verified() {
        return KybVerificationResult.builder().status("VERIFIED").build();
    }

    public static KybVerificationResult rejected(String reason) {
        return KybVerificationResult.builder().status("REJECTED").rejectionReason(reason).build();
    }
}
