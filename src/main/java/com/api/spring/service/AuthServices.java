package com.api.spring.service;

import com.api.spring.repository.UserRepository;
import com.api.spring.security.jwt.JwtTokenProvider;
import com.api.spring.vo.AccountCredentialsVO;
import com.api.spring.vo.TokenVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthServices {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public ResponseEntity<?> signin(AccountCredentialsVO data) {
        try {
            var username = data.getUsername();
            var password = data.getPassword();
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            var user = userRepository.findByUsername(username);
            var tokenResponse = new TokenVO();
            if(user != null) {
                tokenResponse = jwtTokenProvider.createAccessToken(username, user.getRoles());
            }
            else {
                throw new UsernameNotFoundException("Username not found");
            }
            return ResponseEntity.ok(tokenResponse);
        }
        catch(Exception e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    public ResponseEntity<?> refreshToken(String username, String refreshToken) {
            var user = userRepository.findByUsername(username);
            var tokenResponse = new TokenVO();
            if(user != null) {
                tokenResponse = jwtTokenProvider.refreshToken(refreshToken);
            }
            else {
                throw new UsernameNotFoundException("Username not found");
            }
            return ResponseEntity.ok(tokenResponse);
    }
}
