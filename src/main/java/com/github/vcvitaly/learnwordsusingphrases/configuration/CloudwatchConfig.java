package com.github.vcvitaly.learnwordsusingphrases.configuration;

import io.micrometer.cloudwatch2.CloudWatchConfig;
import io.micrometer.cloudwatch2.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;

import java.util.HashMap;
import java.util.Map;

/**
 * CloudwatchConfig.
 *
 * @author Vitalii Chura
 */
@Configuration
public class CloudwatchConfig {

    @Bean
    public CloudWatchAsyncClient cloudWatchAsyncClient() {
        return CloudWatchAsyncClient.create();
    }

    @Bean
    public MeterRegistry getMeterRegistry(CloudwatchProperties cloudwatchProperties) {
        CloudWatchConfig cloudWatchConfig = setupCloudWatchConfig(cloudwatchProperties);

        CloudWatchMeterRegistry cloudWatchMeterRegistry =
                new CloudWatchMeterRegistry(
                        cloudWatchConfig,
                        Clock.SYSTEM,
                        cloudWatchAsyncClient());

        return cloudWatchMeterRegistry;
    }

    private CloudWatchConfig setupCloudWatchConfig(CloudwatchProperties cloudwatchProperties) {
        return key -> cloudwatchProperties.getCloudwatch().get(key.replaceFirst("cloudwatch\\.", ""));
    }

    @ConfigurationProperties
    @ConfigurationPropertiesScan
    private static class CloudwatchProperties {
        @Getter
        private final Map<String, String> cloudwatch = new HashMap<>();
    }
}
