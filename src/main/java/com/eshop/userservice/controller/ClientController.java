package com.eshop.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.eshop.userservice.dto.user.ClientDto;
import com.eshop.userservice.model.Client;
import com.eshop.userservice.model.User;
import com.eshop.userservice.request.client.ClientCreateRequest;
import com.eshop.userservice.request.client.ClientUpdateRequest;
import com.eshop.userservice.request.client.ProfilePictureReq;
import com.eshop.userservice.response.client.ClientCreateResponse;
import com.eshop.userservice.response.client.ClientDeleteResponse;
import com.eshop.userservice.response.client.ClientPageResponse;
import com.eshop.userservice.response.client.ClientUpdateResponse;
import com.eshop.userservice.response.client.ProfilePictureRes;
import com.eshop.userservice.service.ClientService;
import com.eshop.userservice.service.FileService;
import com.eshop.userservice.service.UserService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/users-api/clients")
@RequiredArgsConstructor
public class ClientController {
    private final UserService userService;
    private final ClientService clientService;
    private final FileService fileService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ClientCreateResponse> createClient(@RequestBody @Valid ClientCreateRequest request,
            BindingResult bindingResult) {
        List<String> errors = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            errors = bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
        }
        if (userService.emailExists(request.getEmail())) {
            errors.add("Email already exists");
        }
        if (!userService.confirmPassword(request.getPassword(), request.getConfirm_password())) {
            errors.add("Password confirmation does not match");
        }
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(ClientCreateResponse.builder().errors(errors).build());
        }

        clientService.saveClient(request);

        return ResponseEntity.ok(ClientCreateResponse.builder().success("Client created successfully").build());
    }

    @GetMapping
    public ResponseEntity<ClientPageResponse> getClientsByPagination(@RequestParam(defaultValue = "1") int page) {
        int size = 5;
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ClientDto> clientPage = clientService.getClientsByPagination(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(ClientPageResponse.builder()
                .clients(clientPage.getContent())
                .currentPage(clientPage.getNumber() + 1)
                .totalPages(clientPage.getTotalPages())
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ClientDeleteResponse> deleteClient(@PathVariable Long id) {
        Client client = clientService.findClientById(id);
        if (client == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ClientDeleteResponse.builder()
                    .errors(Collections.singletonList("Client not found")).build());
        }
        if (clientService.deleteClient(id) == false) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ClientDeleteResponse.builder()
                    .errors(Collections.singletonList("Failed to delete client")).build());
        }
        return ResponseEntity.ok(ClientDeleteResponse.builder()
                .success("Client deleted successfully")
                .redirectTo("/clients")
                .build());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ClientDto>> searchClients(
            @RequestParam String search,
            @RequestParam(defaultValue = "1") int page) {
        int size = 5;
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ClientDto> clientPage = clientService.searchClients(search, pageable);
        return ResponseEntity.ok(clientPage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientUpdateResponse> putAdmin(@PathVariable Long id,
            @RequestBody @Valid ClientUpdateRequest request, BindingResult bindingResult) {
        List<String> errors = new ArrayList<>();

        Client client = clientService.findClientById(id);
        if (client == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ClientUpdateResponse.builder()
                    .errors(Collections.singletonList("Client not found")).build());
        } else {
            if (bindingResult.hasErrors()) {
                errors = bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage())
                        .collect(Collectors.toList());
            }
            if (request.getPassword() != null) {
                if (!userService.confirmPassword(request.getPassword(), request.getConfirm_password())) {
                    errors.add("Password confirmation does not match");
                } else {
                    request.setPassword(userService.bcryptPassword(request.getPassword()));
                }
            }
            if (!errors.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ClientUpdateResponse.builder().errors(errors).build());
            }

            User user = client.getUser();
            user.setFirstname(userService.firstLetterToUpperCase(request.getFirstname()));
            user.setLastname(userService.firstLetterToUpperCase(request.getLastname()));
            user.setEmail(request.getEmail().toLowerCase());
            user.setPhone(request.getPhone());
            if (request.getPassword() != null) {
                user.setPassword(request.getPassword());
            }
            client.setUser(user);
            clientService.updateClient(id, client);
        }
        return ResponseEntity.ok(ClientUpdateResponse.builder()
        .success("Client updated successfully")
        .redirectTo("/clients")
        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Client client = clientService.findClientById(id);
        return ResponseEntity.ok(client);
    }
}
