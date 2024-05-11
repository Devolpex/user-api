package com.eshop.userbackend.controller;

import com.eshop.userbackend.enums.Role;
import com.eshop.userbackend.model.User;
import com.eshop.userbackend.request.auth.LoginRequest;
import com.eshop.userbackend.request.auth.RegisterRequest;
import com.eshop.userbackend.response.auth.AuthResponse;
import com.eshop.userbackend.response.auth.LoginResponse;
import com.eshop.userbackend.service.AuthService;
import com.eshop.userbackend.service.JwtService;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users-api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")

public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){
        //
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        User user = authService.login(request);
        List<String> errors = new ArrayList<>();

        if(user == null){
            errors.add("Invalid email or password");
        }
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(LoginResponse.builder().errors(errors).build());
        }
        var jwtToken = jwtService.generateToken(user);
        String redirectTo = new String();
        if (user.getRole() == Role.CLIENT) {
            redirectTo = "/profile";
        }
        if (user.getRole() == Role.ADMIN) {
            redirectTo = "/clients";
        }
        
        return ResponseEntity.ok(LoginResponse.builder()
            .token(jwtToken)
            .role(user.getRole())
            .success("Login successful")
            .redirectTo(redirectTo)
            .build());

    }
}
