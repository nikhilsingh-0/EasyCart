package com.ecom.order.client;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

@Component
public class GatewayHeaderPropagationInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attrs != null) {
            HttpServletRequest incomingRequest = attrs.getRequest();

            String userId = incomingRequest.getHeader("X-User-Id");
            String roles = incomingRequest.getHeader("X-User-Roles");

            if (userId != null) {
                request.getHeaders().set("X-User-Id", userId);
            }
            if (roles != null) {
                request.getHeaders().set("X-User-Roles", roles);
            }
        }

        return execution.execute(request, body);
    }
}
