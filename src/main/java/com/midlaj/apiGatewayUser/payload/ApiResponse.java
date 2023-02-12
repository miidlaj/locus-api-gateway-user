package com.midlaj.apiGatewayUser.payload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class ApiResponse {
    private boolean success;
    private String message;

    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

}