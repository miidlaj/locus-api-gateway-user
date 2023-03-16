package com.midlaj.apiGatewayUser.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDTO {

    @NotNull(message = "The category name must not be empty")
    @Size(max = 50, message = "Category name too long. Please enter maximum number of 50 characters.")
    @Size(min = 3, message = "Category name too short. Please enter minimum number of 3 characters..")
    private String name;

    @NotNull(message = "The category description must not be empty")
    @Size(max = 150, message = "Category description too long. Please enter maximum number of 150 characters.")
    @Size(min = 3, message = "Category description too short. Please enter minimum number of 3 characters..")
    private String description;

    public String getName() {
        return name.toUpperCase();
    }
}
