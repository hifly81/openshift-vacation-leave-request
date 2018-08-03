package com.redhat.springboot.vacationleave.employee.tracing;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;

import io.opentracing.SpanContext;

public class SpanContextHystrixRequestVariable {
    private static final HystrixRequestVariableDefault<SpanContext> spanContextVariable = new HystrixRequestVariableDefault<>();

    private SpanContextHystrixRequestVariable() {
    }

    public static HystrixRequestVariableDefault<SpanContext> getInstance() {
        return spanContextVariable;
    }
}
