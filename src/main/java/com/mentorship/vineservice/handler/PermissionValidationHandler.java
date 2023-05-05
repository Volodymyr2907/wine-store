package com.mentorship.vineservice.handler;
import com.mentorship.vineservice.handler.annotation.TokenValidation;
import feign.FeignException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Aspect
@Component
public class PermissionValidationHandler {

    @Around("@annotation(tokenValidation)")
    public Object handleException(ProceedingJoinPoint joinPoint, TokenValidation tokenValidation) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (FeignException fe) {
            if (fe.status() == HttpStatus.UNAUTHORIZED.value()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
            throw fe;
        }
    }
}