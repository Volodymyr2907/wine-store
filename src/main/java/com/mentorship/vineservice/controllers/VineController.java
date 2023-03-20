package com.mentorship.vineservice.controllers;


import com.mentorship.vineservice.dto.IdDto;
import com.mentorship.vineservice.domain.Vine;
import com.mentorship.vineservice.dto.PaginationDto;
import com.mentorship.vineservice.services.imp.VineServiceImpl;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VineController {

    private static final String TOTAL_COUNT_HEADER = "X-Total-Count";
    private final VineServiceImpl vineServiceImpl;

    @PostMapping("/vine/create")
    public ResponseEntity<IdDto> createVine(@Valid @RequestBody Vine vine) {

        Long createdVineId = vineServiceImpl.saveVineAndReturnId(vine);
        return new ResponseEntity<>(new IdDto(createdVineId), HttpStatus.CREATED);
    }

    @GetMapping("/vines")
    public ResponseEntity<List<Vine>> getAllVinesWithPagination(@Valid
        @RequestParam(required = false, value = "page", defaultValue = "0") Integer page,
        @RequestParam(required = false, value = "size", defaultValue = "5") Integer size,
        @RequestParam(required = false, value = "name") String name,
        @RequestParam(required = false, value = "grape") String grape,
        @RequestParam(required = false, value = "sugar") String sugar,
        @RequestParam(required = false, value = "color") String color) {
        Page<Vine> result = vineServiceImpl.getVinesWithFilterAndPagination(
            new PaginationDto(page, size), sugar, color, name, grape);

        HttpHeaders headers = new HttpHeaders();
        headers.add(TOTAL_COUNT_HEADER, String.valueOf(result.getTotalElements()));

        return new ResponseEntity<>(result.getContent(), headers, HttpStatus.OK);

    }

}
