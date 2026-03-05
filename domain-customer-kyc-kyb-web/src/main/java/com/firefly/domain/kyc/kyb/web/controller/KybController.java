package com.firefly.domain.kyc.kyb.web.controller;

import com.firefly.domain.kyc.kyb.core.kyb.commands.AttachEvidenceCommand;
import com.firefly.domain.kyc.kyb.core.kyb.services.KybService;
import com.firefly.domain.kyc.kyb.interfaces.dtos.kyb.KybCaseResponse;
import com.firefly.domain.kyc.kyb.interfaces.dtos.kyb.KybResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fireflyframework.orchestration.saga.engine.SagaResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * REST controller for KYB (Know Your Business) lifecycle operations.
 */
@RestController
@RequestMapping("/api/v1/kyb")
@RequiredArgsConstructor
@Tag(name = "KYB", description = "KYB compliance case management and verification endpoints")
public class KybController {

    private final KybService kybService;

    @PostMapping(value = "/cases", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Start KYB", description = "Open a new KYB compliance case for an organization.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "KYB case created",
                    content = @Content(schema = @Schema(implementation = KybCaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)
    })
    public Mono<ResponseEntity<KybCaseResponse>> startKyb() {
        return kybService.startKyb()
                .map(result -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(toKybCaseResponse(result, "KYB case created")));
    }

    @PostMapping(value = "/cases/{caseId}/evidence", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Attach Evidence",
            description = "Attach corporate documents (statutes, registries, shareholder records, POA) to a KYB case.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Evidence attached",
                    content = @Content(schema = @Schema(implementation = KybCaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "Case not found", content = @Content)
    })
    public Mono<ResponseEntity<KybCaseResponse>> attachEvidence(
            @Parameter(description = "KYB case identifier", required = true)
            @PathVariable UUID caseId,
            @Valid @RequestBody AttachEvidenceCommand command) {
        return kybService.attachEvidence(caseId, command)
                .map(result -> ResponseEntity.ok(toKybCaseResponse(result, "Evidence attached")));
    }

    @PostMapping(value = "/cases/{caseId}/docs-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Mark Documents Complete", description = "Mark the KYB dossier as complete for review.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Documents marked complete",
                    content = @Content(schema = @Schema(implementation = KybCaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "Case not found", content = @Content)
    })
    public Mono<ResponseEntity<KybCaseResponse>> markDocsComplete(
            @Parameter(description = "KYB case identifier", required = true)
            @PathVariable UUID caseId) {
        return kybService.markDocsComplete(caseId)
                .map(result -> ResponseEntity.ok(toKybCaseResponse(result, "Documents marked complete")));
    }

    @PostMapping(value = "/cases/{caseId}/verify", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Verify KYB",
            description = "Trigger KYB verification (requires UBOs validated). Sets expiry on success.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Verification completed",
                    content = @Content(schema = @Schema(implementation = KybResultResponse.class))),
            @ApiResponse(responseCode = "404", description = "Case not found", content = @Content)
    })
    public Mono<ResponseEntity<KybResultResponse>> verifyKyb(
            @Parameter(description = "KYB case identifier", required = true)
            @PathVariable UUID caseId) {
        return kybService.verifyKyb(caseId)
                .map(result -> ResponseEntity.ok(toKybResultResponse(caseId, result)));
    }

    @PostMapping(value = "/cases/{caseId}/fail", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Fail KYB",
            description = "Reject KYB due to opaque structure, sanctions, or insufficient documentation.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "KYB failed",
                    content = @Content(schema = @Schema(implementation = KybCaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "Case not found", content = @Content)
    })
    public Mono<ResponseEntity<KybCaseResponse>> failKyb(
            @Parameter(description = "KYB case identifier", required = true)
            @PathVariable UUID caseId) {
        return kybService.failKyb(caseId)
                .map(result -> ResponseEntity.ok(toKybCaseResponse(result, "KYB case failed")));
    }

    @PostMapping(value = "/cases/{caseId}/expire", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Expire KYB", description = "Mark KYB as expired by time.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "KYB expired",
                    content = @Content(schema = @Schema(implementation = KybCaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "Case not found", content = @Content)
    })
    public Mono<ResponseEntity<KybCaseResponse>> expireKyb(
            @Parameter(description = "KYB case identifier", required = true)
            @PathVariable UUID caseId) {
        return kybService.expireKyb(caseId)
                .map(result -> ResponseEntity.ok(toKybCaseResponse(result, "KYB case expired")));
    }

    @PostMapping(value = "/cases/{caseId}/renew", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Renew KYB", description = "Renew KYB level and expiry.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "KYB renewed",
                    content = @Content(schema = @Schema(implementation = KybCaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "Case not found", content = @Content)
    })
    public Mono<ResponseEntity<KybCaseResponse>> renewKyb(
            @Parameter(description = "KYB case identifier", required = true)
            @PathVariable UUID caseId) {
        return kybService.renewKyb(caseId)
                .map(result -> ResponseEntity.ok(toKybCaseResponse(result, "KYB case renewed")));
    }

    private KybCaseResponse toKybCaseResponse(SagaResult result, String message) {
        return KybCaseResponse.builder()
                .status(result.isSuccess() ? "SUCCESS" : "FAILED")
                .message(message)
                .build();
    }

    private KybResultResponse toKybResultResponse(UUID caseId, SagaResult result) {
        String verificationStatus = result.resultOf("evaluateResult", Object.class)
                .map(Object::toString)
                .orElse("UNKNOWN");
        return KybResultResponse.builder()
                .caseId(caseId)
                .verificationStatus(verificationStatus)
                .success(result.isSuccess())
                .message(result.isSuccess() ? "Verification completed" : "Verification failed")
                .build();
    }
}
