package com.midlaj.apiGatewayUser.payload;

import com.midlaj.apiGatewayUser.model.Role;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {

    private String jwtToken;
    private String email;
    private String name;
    private String phone;
    private String id;

}