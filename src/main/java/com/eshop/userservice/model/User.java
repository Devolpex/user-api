package com.eshop.userservice.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;


import com.eshop.userservice.enums.Role;

import java.util.Collection;
import java.util.Date;
import java.util.List;  

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String lastname;
    private String firstname;
    private String email;
    private String phone;
    private String username;
    private String password;
    private String image;
    private Date created_at;
    @Nullable
    private Date update_at;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Client client;

}
