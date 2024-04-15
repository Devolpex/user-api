package com.eshop.userbackend.controller;

import com.eshop.userbackend.request.client.ClientCreateRequest;
import com.eshop.userbackend.request.client.ProfilePictureReq;
import com.eshop.userbackend.response.client.ClientCreateResponse;
import com.eshop.userbackend.response.client.ProfilePictureRes;
import com.eshop.userbackend.service.ClientService;
import com.eshop.userbackend.service.FileService;
import com.eshop.userbackend.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ClientController {
    private final UserService userService;
    private final ClientService clientService;
    private final FileService fileService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin(origins = "http://localhost:5173")
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

    @PostMapping("/profile-picture") // Adjust the endpoint path as needed
    public ResponseEntity<ProfilePictureRes> uploadProfilePicture(
            @RequestBody @Valid ProfilePictureReq request,
            BindingResult bindingResult,
            @RequestParam("id") long id) throws IOException  { // Get the id as a request parameter
        List<String> errors = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            errors = bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
        }
        if (!fileService.checkIfImage(request.getPicture())) {
            errors.add("Invalid image format");
        }
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(ProfilePictureRes.builder().errors(errors).build());
        }

        File profile_picture = fileService.convertToImage(request.getPicture(), id);
        String path = fileService.uploadToFolder(profile_picture, "src/uploads/profile-pictures");


        return ResponseEntity.ok(ProfilePictureRes.builder()
                .picture(path)
                .success("Upload profile picture successfully")
                .id(id)
                .build());
    }

}
