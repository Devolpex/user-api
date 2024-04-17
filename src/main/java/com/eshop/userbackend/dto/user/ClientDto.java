package com.eshop.userbackend.dto.user;

import com.eshop.userbackend.enums.Auth;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDto {
    private long id;
    private Auth auth;
    private UserDTO user;
}
