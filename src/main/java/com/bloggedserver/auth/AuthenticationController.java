package com.bloggedserver.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @ExceptionHandler({DuplicateUserException.class})
    @ResponseStatus (HttpStatus.CONFLICT)
    public void handleDuplicateUserException() {

    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody GeneralAuthenticationOrRegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody GeneralAuthenticationOrRegisterRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
