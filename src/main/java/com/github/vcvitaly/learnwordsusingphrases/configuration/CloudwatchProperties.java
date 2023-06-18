package com.github.vcvitaly.learnwordsusingphrases.configuration;

import lombok.Getter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * CloudwatchProperties.
 *
 * @author Vitalii Chura
 */
@ConfigurationProperties("cloudwatch")
@ConditionalOnProperty(name = "cloudwatch.enabled", havingValue = "true")
public record CloudwatchProperties(Map<String, String> config) {}
