package com.eshop.userbackend.controller;

import com.eshop.userbackend.request.auth.LoginRequest;
import com.eshop.userbackend.request.auth.RegisterRequest;
import com.eshop.userbackend.response.auth.AuthResponse;
import com.eshop.userbackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/register")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){
        //
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> register(@RequestBody LoginRequest request){
        //
        return ResponseEntity.ok(service.login(request));
    }
}
