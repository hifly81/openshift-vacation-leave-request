package com.redhat.springboot.vacationleave.employee.tracing;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.contrib.spring.web.client.HttpHeadersCarrier;
import io.opentracing.propagation.Format;

public class TracingRestHandlerInterceptor implements ClientHttpRequestInterceptor {

    private Tracer tracer;
    private Optional<SpanContext> spanContext;

    public TracingRestHandlerInterceptor(Tracer tracer, SpanContext spanContext) {
        this.tracer = tracer;
        this.spanContext = Optional.ofNullable(spanContext);
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        spanContext.ifPresent(s -> tracer.inject(s, Format.Builtin.HTTP_HEADERS, new HttpHeadersCarrier(request.getHeaders())));
        return execution.execute(request, body);
    }

}