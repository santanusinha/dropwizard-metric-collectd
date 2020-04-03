package io.appform.dropwizard.metrics.collectd;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import com.codahale.metrics.collectd.CollectdReporter;
import com.codahale.metrics.collectd.SecurityLevel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.metrics.MetricsFactory;
import io.dropwizard.metrics.ReporterFactory;
import io.dropwizard.validation.BaseValidator;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.File;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
public class CollectdReporterFactoryTest {

    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = BaseValidator.newValidator();

    @Test
    public void isDiscoverable() throws Exception {
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(CollectdReporterFactory.class);
    }

    @Test
    public void minimalConfigurationRequiresHostAndPort() throws Exception {
        CollectdReporterFactory factory = new CollectdReporterFactory();
        factory.setHost("somehost");
        Set<ConstraintViolation<CollectdReporterFactory>> violations = validator.validate(factory);
        assertThat(!violations.isEmpty());

        factory.setPort(2048);
        violations = validator.validate(factory);
        assertThat(violations).isEmpty();
    }

    @Test
    public void securityIsDisabledPerDefault() throws Exception {
        CollectdReporterFactory factory = new CollectdReporterFactory();
        assertThat(factory.getSecurityLevel()).isEqualTo(SecurityLevel.NONE);
    }

    @Test
    public void securityRequiresUsernameAndPassword() throws Exception {
        CollectdReporterFactory factory = new CollectdReporterFactory();
        factory.setHost("somehost");
        factory.setPort(2004);
        factory.setSecurityLevel(SecurityLevel.ENCRYPT);

        Set<ConstraintViolation<CollectdReporterFactory>> violations = validator.validate(factory);

        assertThat(violations).hasSize(1);
        ConstraintViolation<CollectdReporterFactory> violation = violations.iterator().next();
        assertThat(violation.getMessage()).contains("username and password are required");
    }

    @Test
    public void shouldBeConfigurableViaYamlFile() throws Exception {
        YamlConfigurationFactory<MetricsFactory> configFactory = new YamlConfigurationFactory<>(
                MetricsFactory.class, validator, objectMapper, "dw");
        MetricsFactory config = configFactory.build(new File(Resources.getResource("yaml/metrics.yml").toURI()));

        ImmutableList<ReporterFactory> reporterFactories = config.getReporters();
        assertThat(reporterFactories).hasSize(1);
        CollectdReporterFactory reporterFactory = (CollectdReporterFactory) reporterFactories.iterator().next();
        assertThat(reporterFactory.getHost()).isEqualTo("localhost");
        assertThat(reporterFactory.getPort()).isEqualTo(2004);
        assertThat(reporterFactory.getLocalHost()).isEqualTo("app01");
        assertThat(reporterFactory.getSecurityLevel()).isEqualTo(SecurityLevel.ENCRYPT);
        assertThat(reporterFactory.getUsername()).isEqualTo("test");
        assertThat(reporterFactory.getPassword()).isEqualTo("shared-secret");
        ScheduledReporter reporter = reporterFactory.build(new MetricRegistry());
        assertThat(reporter).isInstanceOf(CollectdReporter.class);
    }

}