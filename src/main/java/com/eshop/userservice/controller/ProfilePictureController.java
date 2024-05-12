package com.eshop.userservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eshop.userservice.request.client.ProfilePictureReq;
import com.eshop.userservice.response.client.ProfilePictureRes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/users-api/picture")
@RequiredArgsConstructor
public class ProfilePictureController {

    @PostMapping
    public ResponseEntity<ProfilePictureRes> addProfilePicture(@RequestBody @Valid ProfilePictureReq  request,BindingResult bindingResult) {
        List<String> errors = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            errors = bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList());
        }
        if (!errors.isEmpty()){
            return ResponseEntity.badRequest().body(ProfilePictureRes.builder().errors(errors).build());
        }
        return ResponseEntity.ok(ProfilePictureRes.builder()
            .picture(request.getPicture())
            .success("success")
            .build());
    }

}
