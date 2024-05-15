package com.eshop.userservice.response.auth;

import java.util.ArrayList;
import java.util.List;

import com.eshop.userservice.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private Role role;
    private String success;
    private String redirectTo;
    private List<String> errors = new ArrayList<>();
    
}
