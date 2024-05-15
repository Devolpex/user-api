package com.eshop.userservice.dto.user;

import lombok.*;

import java.util.Date;

import com.eshop.userservice.enums.Auth;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDto {
    private long id;
    private Auth auth;
    private UserDTO user;

}
