package com.firefly.domain.kyc.kyb.infra;

import com.firefly.core.kycb.sdk.api.*;
import com.firefly.core.kycb.sdk.invoker.ApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Factory that wires the core-common-kycb-mgmt SDK.
 * <p>
 * Provides Spring beans for all KYC/KYB core APIs used by the domain service.
 * </p>
 */
@Component
public class KycbClientFactory {

    private final ApiClient apiClient;

    /**
     * Creates the client factory and configures the {@link ApiClient}
     * with the base path from application properties.
     *
     * @param properties the KYCB management connection properties
     */
    public KycbClientFactory(KycbClientConfigurationProperties properties) {
        this.apiClient = new ApiClient();
        this.apiClient.setBasePath(properties.getBasePath());
    }

    /** Creates the compliance cases API bean. */
    @Bean
    public ComplianceCasesApi complianceCasesApi() {
        return new ComplianceCasesApi(apiClient);
    }

    /** Creates the corporate documents API bean. */
    @Bean
    public CorporateDocumentsApi corporateDocumentsApi() {
        return new CorporateDocumentsApi(apiClient);
    }

    /** Creates the UBO management API bean. */
    @Bean
    public UboManagementApi uboManagementApi() {
        return new UboManagementApi(apiClient);
    }

    /** Creates the KYB verification API bean. */
    @Bean
    public KybVerificationApi kybVerificationApi() {
        return new KybVerificationApi(apiClient);
    }

    /** Creates the KYC verification API bean. */
    @Bean
    public KycVerificationApi kycVerificationApi() {
        return new KycVerificationApi(apiClient);
    }

    /** Creates the KYC verification documents API bean. */
    @Bean
    public KycVerificationDocumentsApi kycVerificationDocumentsApi() {
        return new KycVerificationDocumentsApi(apiClient);
    }

    /** Creates the verification documents API bean. */
    @Bean
    public VerificationDocumentsApi verificationDocumentsApi() {
        return new VerificationDocumentsApi(apiClient);
    }
}
