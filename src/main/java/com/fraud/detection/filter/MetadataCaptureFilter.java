package com.fraud.detection.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class MetadataCaptureFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }
        
        String userAgent = request.getHeader("User-Agent");
        
        // In a real scenario, this metadata would be attached to the current security context
        // or stored temporarily for the Risk Engine to evaluate during authentication.
        request.setAttribute("capturedIpAddress", ipAddress);
        request.setAttribute("capturedUserAgent", userAgent);
        
        filterChain.doFilter(request, response);
    }
}
