package ch.xavier.common.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Value("${application.name}")
    private String applicationName;

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> prefix() {
        return r -> r.config().commonTags("application", applicationName);
    }
}
