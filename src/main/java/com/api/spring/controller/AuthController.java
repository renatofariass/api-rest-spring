package com.api.spring.controller;

import com.api.spring.exceptions.InvalidJwtAuthenticationException;
import com.api.spring.service.AuthServices;
import com.api.spring.vo.AccountCredentialsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthServices authServices;

    @Operation(summary = "Authenticates a user and returns a token")
    @PostMapping(value = "/signin")
    public ResponseEntity signin(@RequestBody AccountCredentialsVO data) {
        if (checkIfParamsIsNotNull(data)) {
            throw new InvalidJwtAuthenticationException("Invalid client request");
        }
        var token = authServices.signin(data);
        if (token == null) {
            throw new InvalidJwtAuthenticationException("Invalid client request");
        }
        return token;
    }

    @Operation(summary = "Refresh token for authenticated user and returns a token")
    @PutMapping(value = "/refresh/{username}")
    public ResponseEntity signin(@PathVariable("username") String username,
                                 @RequestHeader("Authorization") String refreshToken) {
        if (checkIfParamsIsNotNull(username, refreshToken)) {
            throw new InvalidJwtAuthenticationException("Invalid client request");
        }
        var token = authServices.refreshToken(username, refreshToken);
        if (token == null) throw new InvalidJwtAuthenticationException("Invalid client request");
        return token;
    }

    private static boolean checkIfParamsIsNotNull(String username, String refreshToken) {
        return refreshToken == null || refreshToken.isBlank() || username == null || username.isBlank();
    }

    private boolean checkIfParamsIsNotNull(AccountCredentialsVO data) {
        return data == null || data.getUsername() == null || data.getUsername().isBlank()
                || data.getPassword() == null || data.getPassword().isBlank();
    }
}

