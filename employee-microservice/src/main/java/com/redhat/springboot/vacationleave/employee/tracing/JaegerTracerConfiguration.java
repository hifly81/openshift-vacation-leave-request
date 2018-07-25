package com.redhat.springboot.vacationleave.employee.tracing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.uber.jaeger.Configuration.ReporterConfiguration;

import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

@Configuration
public class JaegerTracerConfiguration {

    Logger log = LoggerFactory.getLogger(JaegerTracerConfiguration.class);

    @Autowired
    private JaegerTracerProperties properties;

    @Bean
    public Tracer getTracer() {
        String serviceName = properties.getServiceName();
        if (serviceName == null || serviceName.isEmpty()) {
            log.info("No Service Name set. Skipping initialization of the Jaeger Tracer.");
            return GlobalTracer.get();
        }

        com.uber.jaeger.Configuration configuration = new com.uber.jaeger.Configuration(
                serviceName,
                new com.uber.jaeger.Configuration.SamplerConfiguration(
                        properties.getSamplerType(),
                        properties.getSamplerParam(),
                        properties.getSamplerManagerHostPort()),
                new ReporterConfiguration(
                        properties.isReporterLogSpans(),
                        properties.getAgentHost(),
                        properties.getAgentPort(),
                        properties.getReporterFlushInterval(),
                        properties.getReporterMaxQueueSize()
                )
        );
        return configuration.getTracer();
    }

}