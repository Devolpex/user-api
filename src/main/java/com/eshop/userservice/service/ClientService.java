package com.eshop.userservice.service;

import java.util.Date;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eshop.userservice.dto.user.ClientDto;
import com.eshop.userservice.dto.user.UserDTO;
import com.eshop.userservice.enums.Auth;
import com.eshop.userservice.enums.Role;
import com.eshop.userservice.model.Client;
import com.eshop.userservice.model.User;
import com.eshop.userservice.repository.ClientRepository;
import com.eshop.userservice.repository.UserRepository;
import com.eshop.userservice.request.client.ClientCreateRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
    public List<Client> getAllClients(){
        return clientRepository.findAll();
    }


    private ClientDto convertToDTO(Client client) {
        UserDTO user = UserDTO.builder()
                .id(client.getUser().getId())
                .lastname(client.getUser().getLastname())
                .firstname(client.getUser().getFirstname())
                .email(client.getUser().getEmail())
                .phone(client.getUser().getPhone())
                .image(client.getUser().getImage())
                .created_at(client.getUser().getCreated_at())
                .build();
        ClientDto clientDto = ClientDto.builder()
                .id(client.getId())
                .auth(client.getAuth())
                .user(user)
                .build();
        return clientDto;
    }

    public Client findClientById(long id) {
        Client query = clientRepository.findById(id).orElse(null);
        if (query == null) {
            return null;
        }
        User user = User.builder()
                .id(query.getUser().getId())
                .firstname(query.getUser().getFirstname())
                .lastname(query.getUser().getLastname())
                .email(query.getUser().getEmail())
                .phone(query.getUser().getPhone())
                .role(query.getUser().getRole())
                .created_at(query.getUser().getCreated_at())
                .build();
        Client client = Client.builder()
            .id(query.getId())
            .auth(query.getAuth())
            .user(user)
            .build();
        return client;
    }

    public boolean deleteClient(long id) {
        Client client = clientRepository.findById(id).orElse(null);
        if (client != null) {
            clientRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public Page<ClientDto> getClientsByPagination(Pageable pageable) {
        Page<Client> clients = clientRepository.findAll(pageable);
        return clients.map(this::convertToDTO);
    }
    //search client by email first or last name
    public Page<ClientDto> searchClients(String search, Pageable pageable) {
        Page<Client> clients = clientRepository.findByEmailOrFirstNameOrLastName(search, pageable);
        return clients.map(this::convertToDTO);
    }

    public void updateClient(Long id, Client client) {
        User user = client.getUser();
        user.setUpdate_at(new Date());
        userRepository.save(user);
    }

}


