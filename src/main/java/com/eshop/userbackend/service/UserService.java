package com.eshop.userbackend.service;

import com.eshop.userbackend.dto.user.UserCreateDto;
import com.eshop.userbackend.model.User;
import com.eshop.userbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public void saveUser(UserCreateDto userCreateDto){
        // Bcrypt Password
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(userCreateDto.getPassword());
        // Formule the data
        String first_letter_upper_case_first_name = userCreateDto.getFirst_name().substring(0,1).toUpperCase() + userCreateDto.getFirst_name().substring(1);
        String first_letter_upper_case_last_name = userCreateDto.getLast_name().substring(0,1).toUpperCase() + userCreateDto.getLast_name().substring(1);
        String lower_case_email = userCreateDto.getEmail().toLowerCase();
        // Save the data
        User user = User.builder()
                .first_name(first_letter_upper_case_first_name)
                .last_name(first_letter_upper_case_last_name)
                .email(lower_case_email)
                .phone(userCreateDto.getPhone())
                .password(hashedPassword)
                .created_at(new Date())
                .build();
        repository.save(user);
    }

    public List<User> findAllUsers(){
        return repository.findAll();
    }

    public boolean emailExists(String email){
        return repository.existsByEmail(email);
    }
    public boolean confirmPassword(String password,String confirm){
        return password.equals(confirm);
    }
}
