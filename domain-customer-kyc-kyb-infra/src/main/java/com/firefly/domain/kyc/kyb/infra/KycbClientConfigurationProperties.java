package com.firefly.domain.kyc.kyb.infra;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Configuration properties for the core-common-kycb-mgmt SDK connection.
 */
@Component
@ConfigurationProperties(prefix = "api-configuration.core-platform.kycb-mgmt")
@Data
public class KycbClientConfigurationProperties {

    /** Base URL of the core-common-kycb-mgmt API. */
    private String basePath;

    /** HTTP client timeout. */
    private Duration timeout = Duration.ofSeconds(10);
}
