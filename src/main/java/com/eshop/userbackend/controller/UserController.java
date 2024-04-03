package com.eshop.userbackend.controller;

import com.eshop.userbackend.dto.user.UserCreateDto;
import com.eshop.userbackend.model.User;
import com.eshop.userbackend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserCreateDto userCreateDto, BindingResult bindingResult){
        Map<String, Object> errorsResponse = new HashMap<>();
        List<String> validationsErrors = new ArrayList<>();

        if (bindingResult.hasErrors()) {
            validationsErrors = bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList());
        }
        if (service.emailExists(userCreateDto.getEmail())){
            validationsErrors.add("Email alredy exists");
        }
        if (!service.confirmPassword(userCreateDto.getPassword(),userCreateDto.getConfirm_password())){
            validationsErrors.add("Password confirmation not mush");
        }
        if (!validationsErrors.isEmpty()){
            errorsResponse.put("errors",validationsErrors);
            return ResponseEntity.badRequest().body(errorsResponse);
        }
        service.saveUser(userCreateDto);
        Map<String,String> successMessage = new HashMap<>();
        successMessage.put("success","User created successfuly");
        return ResponseEntity.ok(successMessage);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(service.findAllUsers());
    }
}
