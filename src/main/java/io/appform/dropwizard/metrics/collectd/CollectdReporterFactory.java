package io.appform.dropwizard.metrics.collectd;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import com.codahale.metrics.collectd.CollectdReporter;
import com.codahale.metrics.collectd.SecurityLevel;
import com.codahale.metrics.collectd.Sender;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.metrics.BaseReporterFactory;
import io.dropwizard.validation.OneOf;
import io.dropwizard.validation.ValidationMethod;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@JsonTypeName("collectd")
public class CollectdReporterFactory extends BaseReporterFactory {

    @NotNull
    private String host;

    @NotNull @Min(1L) @Max(65535L)
    private int port;
    
    private String localHost;

    @OneOf({"NONE", "SIGN", "ENCRYPT"})
    private SecurityLevel securityLevel = SecurityLevel.NONE;

    private String username;

    private String password;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getLocalHost() {
        return localHost;
    }

    public void setLocalHost(String localHost) {
        this.localHost = localHost;
    }

    public SecurityLevel getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(SecurityLevel level) {
        this.securityLevel = level;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ValidationMethod(message = 
        "username and password are required when using security level SIGN or ENCRYPT.")
    boolean isSecurityLevelConfiguredCorrectly() {
        boolean credentialsRequired = (securityLevel != SecurityLevel.NONE);
        if (credentialsRequired) {
            return isNotBlank(username) && isNotBlank(password);
        } else {
            return true;
        }
    }

    private static boolean isNotBlank(String str) {
        return str != null && ! str.trim().isEmpty();
    }

    @Override
    public ScheduledReporter build(MetricRegistry registry) {
        return CollectdReporter.forRegistry(registry)
                .withHostName(localHost)
                .withSecurityLevel(getSecurityLevel())
                .withUsername(username)
                .withPassword(password)
                .convertDurationsTo(getDurationUnit())
                .convertRatesTo(getRateUnit())
                .filter(getFilter())
                .build(new Sender(host, port));
    }

}
