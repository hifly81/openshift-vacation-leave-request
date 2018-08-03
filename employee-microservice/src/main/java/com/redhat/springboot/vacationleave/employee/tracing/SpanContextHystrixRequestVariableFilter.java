package com.redhat.springboot.vacationleave.employee.tracing;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

public class SpanContextHystrixRequestVariableFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Tracer tracer = GlobalTracer.get();
        Optional.ofNullable(tracer.activeSpan()).map(a -> a.context())
            .ifPresent(s -> SpanContextHystrixRequestVariable.getInstance().set(s));
        filterChain.doFilter(request, response);
    }

}
