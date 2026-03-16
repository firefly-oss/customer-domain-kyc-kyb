package com.firefly.domain.kyc.kyb.web.controllers;

import com.firefly.domain.kyc.kyb.core.kyb.commands.AttachEvidenceCommand;
import com.firefly.domain.kyc.kyb.core.kyb.services.KybService;
import com.firefly.domain.kyc.kyb.core.kyb.workflows.KybWorkflowInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.UUID;

/**
 * REST controller for Know-Your-Business (KYB) verification operations.
 *
 * <p>Exposes endpoints to manage the full KYB lifecycle: opening cases
 * for organizations, attaching statutes and registries, marking dossiers
 * complete, verifying (with UBO validation), failing, expiring, and
 * renewing KYB verifications.
 */
@RestController
@RequestMapping("/api/v1/kyb")
@RequiredArgsConstructor
@Tag(name = "KYB", description = "CQ queries and registration for kyb")
public class KybController {

    private final KybService kybService;

    /**
     * Opens a new KYB process for an organization.
     *
     * @param input the workflow input containing party and business details
     * @return an empty 200 response on success
     */
    @PostMapping("/cases")
    @Operation(summary = "Start KYB", description = "Open a KYB process for an organization")
    public Mono<ResponseEntity<Object>> startKyb(@Valid @RequestBody KybWorkflowInput input) {
        return kybService.startKyb(input)
                .map(result -> ResponseEntity.ok().build());
    }

    /**
     * Attaches statutes, registries, shareholder records, or POA documents to a KYB case.
     *
     * @param caseId  the unique identifier of the KYB case
     * @param command the command containing document metadata and fingerprint reference
     * @return an empty 200 response on success
     */
    @PostMapping("/cases/{caseId}/documents")
    @Operation(summary = "Attach Evidence", description = "Attach statutes, registries, shareholder records, POA")
    public Mono<ResponseEntity<Object>> attachEvidence(@PathVariable UUID caseId,
                                                       @Valid @RequestBody AttachEvidenceCommand command) {
        return kybService.attachEvidence(caseId, command)
                .map(result -> ResponseEntity.ok().build());
    }

    /**
     * Marks the KYB dossier as complete for review.
     *
     * @param caseId the unique identifier of the KYB case
     * @return an empty 200 response on success
     */
    @PostMapping("/cases/{caseId}/docs-complete")
    @Operation(summary = "Mark Documents Complete", description = "Mark KYB dossier complete")
    public Mono<ResponseEntity<Object>> markDocsComplete(@PathVariable UUID caseId) {
        return kybService.markDocsComplete(caseId)
                .map(result -> ResponseEntity.ok().build());
    }

    /**
     * Verifies the entity, requiring UBOs to be validated, and sets expiry.
     *
     * @param caseId  the unique identifier of the KYB case
     * @return an empty 200 response on success
     */
    @PostMapping("/cases/{caseId}/verify")
    @Operation(summary = "Verify KYB", description = "Verify entity (requires UBOs validated) and set expiry")
    public Mono<ResponseEntity<Object>> verifyKyb(@PathVariable UUID caseId) {
        return kybService.verifyKyb(caseId)
                .map(result -> ResponseEntity.ok().build());
    }

    /**
     * Rejects the KYB case due to opaque structure, sanctions, or insufficient documentation.
     *
     * @param caseId  the unique identifier of the KYB case
     * @return an empty 200 response on success
     */
    @PostMapping("/cases/{caseId}/fail")
    @Operation(summary = "Fail KYB", description = "Reject KYB (opaque structure/sanctions/insufficient docs)")
    public Mono<ResponseEntity<Object>> failKyb(@PathVariable UUID caseId) {
        return kybService.failKyb(caseId)
                .map(result -> ResponseEntity.ok().build());
    }

    /**
     * Marks the KYB verification as expired by time.
     *
     * @param caseId the unique identifier of the KYB case
     * @return an empty 200 response on success
     */
    @PostMapping("/cases/{caseId}/expire")
    @Operation(summary = "Expire KYB", description = "Mark KYB expired by time")
    public Mono<ResponseEntity<Object>> expireKyb(@PathVariable UUID caseId) {
        return kybService.expireKyb(caseId)
                .map(result -> ResponseEntity.ok().build());
    }

    /**
     * Renews the KYB verification with a new level and expiry.
     *
     * @param caseId the unique identifier of the KYB case
     * @return an empty 200 response on success
     */
    @PostMapping("/cases/{caseId}/renew")
    @Operation(summary = "Renew KYB", description = "Renew KYB level/expiry")
    public Mono<ResponseEntity<Object>> renewKyb(@PathVariable UUID caseId) {
        return kybService.renewKyb(caseId)
                .map(result -> ResponseEntity.ok().build());
    }
}
