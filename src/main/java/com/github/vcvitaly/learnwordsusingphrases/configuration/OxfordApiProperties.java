package com.github.vcvitaly.learnwordsusingphrases.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * OxfordApiProperties.
 *
 * @author Vitalii Chura
 */
@ConfigurationProperties("api.oxford")
public record OxfordApiProperties(String appId, String appKey) {}
