package com.blogapp.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // Run before Spring Security
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        
        // Skip spammy endpoints like health checks or swagger assets
        if (uri.startsWith("/actuator") || uri.contains("swagger") || uri.contains("api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        long startTime = System.currentTimeMillis();
        String method = request.getMethod();
        String remoteAddr = request.getRemoteAddr();

        // Optional: Include query string
        String queryString = request.getQueryString();
        String fullUri = queryString != null ? uri + "?" + queryString : uri;

        log.info("--> Incoming Request: [{}] {} from {}", method, fullUri, remoteAddr);

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            int status = response.getStatus();
            
            if (status >= 400 && status < 500) {
                log.warn("<-- Completed Request: [{}] {} with status {} in {} ms", method, fullUri, status, duration);
            } else if (status >= 500) {
                log.error("<-- Failed Request: [{}] {} with status {} in {} ms", method, fullUri, status, duration);
            } else {
                log.info("<-- Completed Request: [{}] {} with status {} in {} ms", method, fullUri, status, duration);
            }
        }
    }
}
