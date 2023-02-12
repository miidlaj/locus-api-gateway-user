package com.midlaj.apiGatewayUser.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String name;


    @NotBlank
    private String password;

    @NotBlank
    private String phone;


}