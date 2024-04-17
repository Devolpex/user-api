package com.eshop.userbackend.dto.user;

import com.eshop.userbackend.enums.Auth;
import lombok.*;

import java.util.Date;

@Data

@Getter
@Setter
@NoArgsConstructor
public class ClientDto {
    private long id;
    private Auth auth;
    private UserDTO user;

    public ClientDto(long id, Auth auth, UserDTO user) {
       this.id = id;
       this.auth = auth;
       this.user =user;

    }

}
