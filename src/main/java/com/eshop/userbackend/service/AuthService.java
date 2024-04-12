package com.eshop.userbackend.service;

import com.eshop.userbackend.enums.Role;
import com.eshop.userbackend.model.Client;
import com.eshop.userbackend.model.CodeConfirmation;
import com.eshop.userbackend.model.User;
import com.eshop.userbackend.repository.CodeConfirmationRepository;
import com.eshop.userbackend.repository.UserRepository;
import com.eshop.userbackend.request.auth.LoginRequest;
import com.eshop.userbackend.request.auth.RegisterRequest;
import com.eshop.userbackend.response.auth.AuthResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Random;

import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CodeConfirmationRepository codeConfirmationRepository;

    public AuthResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CLIENT)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder().token(jwtToken).build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder().token(jwtToken).build();
    }

    public String generateCodeValidation() {
        // Generate a random 6-digit code
        Random random = new Random();
        int min = 100000;
        int max = 999999;
        int code = random.nextInt(max - min + 1) + min;
        return String.valueOf(code);
    }

    // public CodeConfirmation getCodeConfirmation(String email){
    //     CodeConfirmation codeConfirmation = codeConfirmationRepository.findLastCodeConfirmationByClientEmail(email);
    //     return codeConfirmation;
    // }

    public CodeConfirmation getLastCodeConfirmationByEmail(String email) {
        Pageable pageable = PageRequest.of(0, 1, Sort.by("id").descending());
        List<CodeConfirmation> confirmations = codeConfirmationRepository.findLastCodeConfirmationByClientEmail(email, pageable);
        return confirmations.isEmpty() ? null : confirmations.get(0);
    }
    
    public boolean confirmCodeConfirmation(String clientCodeConfirmation,String systemCodeConfirmation){
        if (clientCodeConfirmation.equals(systemCodeConfirmation)){
            return true;
        }
        return false;
    }

    public void deleteCodeConfirmation(String email){
        codeConfirmationRepository.deleteCodeConfirmationsByClientEmail(email);
    }




}
