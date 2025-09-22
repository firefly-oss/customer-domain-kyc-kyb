package com.firefly.domain.kyc.kyb.web.controller;

import com.firefly.domain.kyc.kyb.core.kyb.commands.AttachEvidenceCommand;
import com.firefly.domain.kyc.kyb.core.kyb.services.KybService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/kyb")
@RequiredArgsConstructor
@Tag(name = "KYB", description = "CQ queries and registration for kyb")
public class KybController {

    private final KybService kybService;

    @PostMapping("/cases")
    @Operation(summary = "Start KYB", description = "Open a KYB process for an organization")
    public Mono<ResponseEntity<Object>> startKyb() {
        return kybService.startKyb()
                .map(result -> ResponseEntity.ok().build());
    }

    @PostMapping("/cases/{caseId}/evidence")
    @Operation(summary = "Attach Evidence", description = "Attach statutes, registries, shareholder records, POA")
    public Mono<ResponseEntity<Object>> attachEvidence(@PathVariable UUID caseId,
                                                       @RequestBody AttachEvidenceCommand command) {
        return kybService.attachEvidence(caseId, command)
                .map(result -> ResponseEntity.ok().build());
    }

    @PostMapping("/cases/{caseId}/docs-complete")
    @Operation(summary = "Mark Documents Complete", description = "Mark KYB dossier complete")
    public Mono<ResponseEntity<Object>> markDocsComplete(@PathVariable UUID caseId) {
        return kybService.markDocsComplete(caseId)
                .map(result -> ResponseEntity.ok().build());
    }

    @PostMapping("/cases/{caseId}/verify")
    @Operation(summary = "Verify KYB", description = "Verify entity (requires UBOs validated) and set expiry")
    public Mono<ResponseEntity<Object>> verifyKyb(@PathVariable UUID caseId) {
        return kybService.verifyKyb(caseId)
                .map(result -> ResponseEntity.ok().build());
    }

    @PostMapping("/cases/{caseId}/fail")
    @Operation(summary = "Fail KYB", description = "Reject KYB (opaque structure/sanctions/insufficient docs)")
    public Mono<ResponseEntity<Object>> failKyb(@PathVariable UUID caseId) {
        return kybService.failKyb(caseId)
                .map(result -> ResponseEntity.ok().build());
    }

    @PostMapping("/cases/{caseId}/expire")
    @Operation(summary = "Expire KYB", description = "Mark KYB expired by time")
    public Mono<ResponseEntity<Object>> expireKyb(@PathVariable UUID caseId) {
        return kybService.expireKyb(caseId)
                .map(result -> ResponseEntity.ok().build());
    }

    @PostMapping("/cases/{caseId}/renew")
    @Operation(summary = "Renew KYB", description = "Renew KYB level/expiry")
    public Mono<ResponseEntity<Object>> renewKyb(@PathVariable UUID caseId) {
        return kybService.renewKyb(caseId)
                .map(result -> ResponseEntity.ok().build());
    }
}
