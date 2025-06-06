// src/main/java/com/bcc/washer/config/VeryEarlyHeaderLoggerFilter.java
package com.bcc.washer.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE - 1000) // Ensure it runs even before Spring Security's FilterChainProxy
public class VeryEarlyHeaderLoggerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        System.out.println("\n--- VERY EARLY DEBUG: Incoming Request Headers for " + req.getMethod() + " " + req.getRequestURI() + " ---");
        Enumeration<String> headerNames = req.getHeaderNames();
        if (headerNames != null) {
            for (String headerName : Collections.list(headerNames)) {
                if (headerName.equalsIgnoreCase("authorization")) {
                    System.out.println("AUTHORIZATION: " + req.getHeader(headerName));
                } else {
                    System.out.println(headerName + ": " + req.getHeader(headerName));
                }
            }
        }
        System.out.println("--- END VERY EARLY DEBUG ---\n");
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}
    @Override
    public void destroy() {}
}