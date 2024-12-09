package com.example.app.views.interceptor;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.security.enterprise.SecurityContext;
import lombok.extern.java.Log;

import java.io.Serializable;

@Interceptor
@LoggerInterceptor
@Priority(Interceptor.Priority.APPLICATION)
@Log
public class CustomLoggerInterceptor implements Serializable {
    private static final long serialVersionUID = 1L;
    private final SecurityContext securityContext;

    @Inject
    public CustomLoggerInterceptor(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    @AroundInvoke
    public Object invoke(InvocationContext context) throws Exception {
        log.info(securityContext.getCallerPrincipal().getName() + context.getMethod().getName());
        return context.proceed();
    }
}
