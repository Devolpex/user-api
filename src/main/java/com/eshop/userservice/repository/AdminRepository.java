package com.eshop.userservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.eshop.userservice.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long>{
    Page<Admin> findAll(Pageable pageable);


}
