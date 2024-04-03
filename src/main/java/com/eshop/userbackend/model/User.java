package com.eshop.userbackend.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue
    private long id;
    private String last_name;
    private String first_name;
    private String email;
    private String phone;
    private String password;
    private String image_url;
    private Date created_at;
    @Nullable
    private Date update_at;
    @Nullable
    private Date deleted_at;

}
