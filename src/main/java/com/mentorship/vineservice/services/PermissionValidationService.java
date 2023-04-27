package com.mentorship.vineservice.services;

import com.mentorship.vineservice.controllers.exeptions.VinePermissionException;
import com.mentorship.vineservice.dto.enums.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Component
public class PermissionValidationService {

    private static final String YOU_HAVE_NO_PERMISSION_TO_DO_THIS_ACTION = "You have no permission to do this action.";


    public void validateUserPermission(String requestHeader, UserRole role) throws VinePermissionException {
        try {
            String token = getTokenFromRequest(requestHeader);
            if (!checkIfUserExist(token, role.name())) {
                throw new VinePermissionException(HttpStatus.FORBIDDEN, YOU_HAVE_NO_PERMISSION_TO_DO_THIS_ACTION);
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

    public Boolean checkIfUserExist(String token, String role) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8384/api/auth-service/permission/{token}/{role}";
        return restTemplate.getForObject(url, Boolean.class, token, role);
    }

}
