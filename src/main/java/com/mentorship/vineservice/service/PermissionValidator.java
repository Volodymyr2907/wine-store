package com.mentorship.vineservice.service;

import com.mentorship.vineservice.controller.exeption.PermissionException;
import com.mentorship.vineservice.dto.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

@Component
public class PermissionValidator {

    @Autowired
    private PermissionClient permissionClient;


    public void validateUserPermission(String requestHeader, UserRole role) throws PermissionException {
        try {
            String token = getTokenFromRequest(requestHeader);
            if (!doesUserExist(token, role.name())) {
                throw new PermissionException(HttpStatus.FORBIDDEN, "You have no permission to do this action.");
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
        return permissionClient.getUserPermission(token, role);
    }

}
