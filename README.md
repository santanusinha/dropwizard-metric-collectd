
# Dropwizard Metrics Collectd Integration

## What it is

An artifact providing support for pushing metrics from your Dropwizard app
to Collectd via UDP.

Allows you to use the Collectd metric reporter without any custom code.
Configuration is done through the application configuration file.

## Usage

Add the dependency to your project:

    <dependency>
        <groupId>me.g-fresh</groupId>
        <artifactId>dropwizard-metrics-collectd</artifactId>
        <version>${dropwizard-metrics-collectd.version}</version>
    </dependency>

Configure the collectd reporter in the `metrics` section of the dropwizard
application configuration file:

    metrics:
      reporters:
      - type: collectd
        host: localhost
        port: 2004

The example given above uses the minimal configuration.

For the full list of options specific to the collectd reporter, see the [API documentation](http://www.javadoc.io/doc/me.g-fresh/dropwizard-metrics-collectd/me/g_fresh/dropwizard/metrics/collectd/CollectdReporterFactory.html)

Additional common options can be specified as described in the
[Dropwizard Manual](http://www.dropwizard.io/1.3.1/docs/manual/configuration.html#all-reporters)
