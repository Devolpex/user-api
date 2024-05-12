package com.eshop.userservice.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eshop.userservice.enums.Role;
import com.eshop.userservice.model.Admin;
import com.eshop.userservice.model.User;
import com.eshop.userservice.repository.AdminRepository;
import com.eshop.userservice.repository.UserRepository;
import com.eshop.userservice.request.admin.AdminCreateRequest;
import com.eshop.userservice.request.admin.AdminUpdateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public void saveAdmin(User user) {
        // Save user data
        userRepository.save(user);
        // Build client data
        Admin admin = Admin.builder()
                .user(user)
                .build();
        // Save client data
        adminRepository.save(admin);
    }

    public Admin findAdminById(long id) {
        Admin query = adminRepository.findById(id).orElse(null);
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
        Admin admin = Admin.builder().id(query.getId()).user(user).build();
        return admin;
    }

    public void updateAdmin(long id,Admin admin){
        User user = admin.getUser();
        user.setUpdate_at(new Date());
        userRepository.save(user);
    }
    public boolean deleteAdmin(long id) {
        Admin admin = adminRepository.findById(id).orElse(null);
        if (admin != null) {
            adminRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Page<Admin> getAdminsByPagination(Pageable pageable) {
        return adminRepository.findAll(pageable);
    }




}
