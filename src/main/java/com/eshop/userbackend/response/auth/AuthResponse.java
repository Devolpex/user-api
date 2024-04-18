package com.eshop.userbackend.response.auth;


import java.util.ArrayList;
import java.util.List;

import com.eshop.userbackend.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private Role role;
    private String success;
    private String redirectTo;
    private List<String> errors = new ArrayList<>();
}
