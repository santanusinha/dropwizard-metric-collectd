
# Dropwizard Metrics Collectd Integration

[![Build Status](https://travis-ci.org/santanusinha/dropwizard-metric-collectd.svg?branch=master)](https://travis-ci.org/github/santanusinha/dropwizard-metric-collectd)

## What it is

An artifact providing support for pushing metrics from your Dropwizard app
to Collectd via UDP.

Allows you to use the Collectd metric reporter without any custom code.
Configuration is done through the application configuration file.

## Usage

Add the dependency to your project:

    <dependency>
        <groupId>io.appform.dropwizard</groupId>
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

Additional common options can be specified as described in the
[Dropwizard Manual](https://www.dropwizard.io/en/release-1.3.x/manual/core.html#metrics)
