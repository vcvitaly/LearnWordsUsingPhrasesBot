package com.github.vcvitaly.learnwordsusingphrases.configuration;

import io.micrometer.cloudwatch2.CloudWatchConfig;
import io.micrometer.cloudwatch2.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;

/**
 * CloudwatchConfig.
 *
 * @author Vitalii Chura
 */
@Configuration
public class CloudwatchConfig {

    @Bean
    @ConditionalOnProperty(name = "cloudwatch.enabled", havingValue = "true")
    public CloudWatchAsyncClient cloudWatchAsyncClient() {
        return CloudWatchAsyncClient.create();
    }

    @Bean
    @ConditionalOnProperty(name = "cloudwatch.enabled", havingValue = "true")
    public MeterRegistry cloudWatchMeterRegistry(CloudwatchProperties cloudwatchProperties) {
        CloudWatchConfig cloudWatchConfig = setupCloudWatchConfig(cloudwatchProperties);

        CloudWatchMeterRegistry cloudWatchMeterRegistry =
                new CloudWatchMeterRegistry(
                        cloudWatchConfig,
                        Clock.SYSTEM,
                        cloudWatchAsyncClient());

        return cloudWatchMeterRegistry;
    }

    @Bean
    @ConditionalOnProperty(name = "cloudwatch.enabled", havingValue = "false")
    public MeterRegistry simpleMeterRegistry() {
        return new SimpleMeterRegistry();
    }

    private CloudWatchConfig setupCloudWatchConfig(CloudwatchProperties cloudwatchProperties) {
        return key -> cloudwatchProperties.getConfig().get(key.replaceFirst("cloudwatch\\.", ""));
    }
}
