package com.mentorship.vineservice.controllers;


import static com.mentorship.vineservice.dto.enums.VineColor.getVineColor;

import com.mentorship.vineservice.controllers.exeptions.VinePermissionException;
import com.mentorship.vineservice.dto.VineDto;
import com.mentorship.vineservice.dto.VinesDto;
import com.mentorship.vineservice.dto.enums.UserRole;
import com.mentorship.vineservice.dto.enums.VineColor;
import com.mentorship.vineservice.model.VinesQueryParameters;
import com.mentorship.vineservice.services.VineService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vine-service")
public class VineController {

    private static final String TOTAL_COUNT_HEADER = "X-Total-Count";
    private static final String YOU_HAVE_NO_PERMISSION_TO_DO_THIS_ACTION = "You have no permission to do this action.";
    private final VineService vineService;

    @PostMapping("/vine")
    public ResponseEntity<Long> createVine(@RequestBody VineDto vine,
        @RequestHeader("Authorization") String requestHeader) throws VinePermissionException {

        String token = getTokenFromRequest(requestHeader);
        if (checkUserPermission(token, UserRole.ADMIN.name())) {
            Long createdVineId = vineService.saveVine(vine);
            return new ResponseEntity<>(createdVineId, HttpStatus.CREATED);
        }
        throw new VinePermissionException(HttpStatus.FORBIDDEN, YOU_HAVE_NO_PERMISSION_TO_DO_THIS_ACTION);
    }

    @GetMapping("/vines")
    public ResponseEntity<List<VineDto>> getAllVinesWithPagination(
        @RequestParam(required = false, value = "page", defaultValue = "0") Integer page,
        @RequestParam(required = false, value = "size", defaultValue = "5") Integer size,
        @RequestParam(required = false, value = "name") String name,
        @RequestParam(required = false, value = "grape") String grape,
        @RequestParam(required = false, value = "sugar") String sugar,
        @RequestParam(required = false, value = "color") String color,
        @RequestParam(required = false, value = "year") Integer year) {

        VinesQueryParameters queryParameters = VinesQueryParameters.builder()
            .page(page)
            .size(size)
            .name(name)
            .grapeName(grape)
            .sugar(sugar)
            .color(getVineColor(color))
            .year(year)
            .build();

        VinesDto vinesDto = vineService.getVinesWithFilterAndPagination(queryParameters);

        HttpHeaders headers = new HttpHeaders();
        headers.add(TOTAL_COUNT_HEADER, String.valueOf(vinesDto.getTotalCount()));

        return new ResponseEntity<>(vinesDto.getVines(), headers, HttpStatus.OK);

    }


    private Boolean checkUserPermission(String token, String role) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8384/api/auth-service/permission/{token}/{role}";
        return restTemplate.getForObject(url, Boolean.class, token, role);
    }


    private String getTokenFromRequest(String request) throws VinePermissionException {

        if (StringUtils.hasText(request) && request.startsWith("Bearer ")) {
            return request.substring(7);
        }
        throw new VinePermissionException(HttpStatus.FORBIDDEN, "Invalid token.");
    }


}
