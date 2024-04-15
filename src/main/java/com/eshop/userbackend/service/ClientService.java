package com.eshop.userbackend.service;

import java.util.Date;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eshop.userbackend.enums.Auth;
import com.eshop.userbackend.enums.Role;
import com.eshop.userbackend.model.Client;
import com.eshop.userbackend.model.User;
import com.eshop.userbackend.repository.ClientRepository;
import com.eshop.userbackend.repository.UserRepository;
import com.eshop.userbackend.request.client.ClientCreateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public void saveClient(ClientCreateRequest request) {
        // Formule the data
        request.setFirstname(request.getFirstname().substring(0, 1).toUpperCase() + request.getFirstname().substring(1));
        request.setLastname(request.getLastname().substring(0, 1).toUpperCase() + request.getLastname().substring(1));
        request.setEmail(request.getEmail().toLowerCase());
        // Bycrypt Password
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        // Build user data
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(request.getPassword())
                .role(Role.CLIENT)
                .created_at(new Date())
                .build();
        // Save user data
        userRepository.save(user);
        // Build client data
        Client client = Client.builder()
                .user(user)
                .auth(Auth.EMAIL)
                .build();
        // Save client data
        clientRepository.save(client);
    }

    public boolean checkIsAuthByEmail(String email){
        Auth authType = clientRepository.findAuthByEmail(email);
        if (authType != Auth.EMAIL) {
            return false;
        }
        return true;

    }

    public Client findClientByEmail(String email){
        Client client = clientRepository.findByUserEmail(email);
        return client;
    }
}
