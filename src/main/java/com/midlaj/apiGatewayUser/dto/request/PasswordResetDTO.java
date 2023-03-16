package com.midlaj.apiGatewayUser.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetDTO {

    @NotBlank(message = "Password is blank")
    @Size(min = 3, message = "Password must have minimum of 8 characters.")
    private String newPassword;

    @NotBlank(message = "Token is blank")
    private String resetPasswordToken;
}
