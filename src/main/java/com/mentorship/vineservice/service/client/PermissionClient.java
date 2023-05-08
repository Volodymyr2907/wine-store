package com.mentorship.vineservice.service.client;

import com.mentorship.vineservice.handler.annotation.TokenValidation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("user-client")
public interface PermissionClient {

    @GetMapping("/api/auth-service/permission/{token}/{role}")
    @TokenValidation
    Boolean getUserPermission(@PathVariable String token, @PathVariable String role);

}
