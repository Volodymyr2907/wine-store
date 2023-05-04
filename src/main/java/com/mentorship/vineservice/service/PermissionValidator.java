package com.mentorship.vineservice.service;

import com.mentorship.vineservice.controller.exeption.VinePermissionException;
import com.mentorship.vineservice.dto.enums.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Component
public class PermissionValidator {


    public void validateUserPermission(String requestHeader, UserRole role) throws VinePermissionException {
        try {
            String token = getTokenFromRequest(requestHeader);
            if (!doesUserExist(token, role.name())) {
                throw new VinePermissionException(HttpStatus.FORBIDDEN, "You have no permission to do this action.");
            }
        } catch (HttpClientErrorException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    public String getTokenFromRequest(String request) {

        if (StringUtils.hasText(request) && request.startsWith("Bearer ")) {
            return request.substring(7);
        }
        throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Not Bearer token");
    }

    public Boolean doesUserExist(String token, String role) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8384/api/auth-service/permission/{token}/{role}";
        return restTemplate.getForObject(url, Boolean.class, token, role);
    }

}
