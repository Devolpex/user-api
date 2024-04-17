package com.eshop.userbackend.dto.user;

import lombok.*;

import java.util.Date;
@Data
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private long id;
    private String lastname;
    private String firstname;
    private String email;
    private String phone;
    private String password;
    private String image;
    private Date created_at;



    public UserDTO(String email, String firstname, String lastname, String image, String password, Date createdAt ,String phone) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.image = image;
        this.created_at = createdAt;
    }
}



