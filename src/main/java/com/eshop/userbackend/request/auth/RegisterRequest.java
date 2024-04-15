package com.eshop.userbackend.request.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String lastname;
    private String firstname;
    private String email;
    private String phone;
    private String password;
    private String confirm_password;
}
