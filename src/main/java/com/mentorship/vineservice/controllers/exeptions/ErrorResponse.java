package com.mentorship.vineservice.controllers.exeptions;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ErrorResponse {

    private HttpStatus status;
    private LocalDateTime timeStamp;
    private List<String> errors;


    public ErrorResponse(HttpStatus status) {
        this();
        this.status = status;
    }


    public ErrorResponse(HttpStatus status, List<String> errors) {
        this();
        this.status = status;
        this.errors = errors;
    }

    public ErrorResponse() {
        timeStamp = LocalDateTime.now();
    }

    public ErrorResponse(HttpStatus status, LocalDateTime timeStamp, List<String> errors) {
        this();
        this.status = status;
        this.timeStamp = timeStamp;
        this.errors = errors;
    }

}
