package com.midlaj.apiGatewayUser.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResortRequestDTO {

    @NotNull(message = "The resort name must not be empty")
    @Size(max = 50, message = "Resort name too long. Please enter maximum number of 50 characters.")
    @Size(min = 3, message = "Resort name too short. Please enter minimum number of 3 characters..")
    private String resortName;

    @NotNull(message = "The resort description must not be empty")
    @Size(max = 50, message = "Resort description too long. Please enter maximum number of 50 characters.")
    @Size(min = 3, message = "Resort description too short. Please enter minimum number of 3 characters..")
    private String description;

    @NotNull(message = "The user id must not be empty")
    private Long userId;


    @NotNull(message = "The resort category id must not be empty")
    private Long categoryId;

    @NotNull(message = "The facility id's must not be empty")
    private Long[] facilityIds;

    @NotNull(message = "The zip code must not be empty")
    @Pattern(regexp = "^\\d{1,6}$", flags = { Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.MULTILINE }, message = "The Zip code is invalid.")
    private String zipCode;

    @NotNull(message = "The city must not be empty")
    @Size(max = 50, message = "city is too long. Please enter maximum number of 50 characters.")
    @Size(min = 3, message = "city is too short. Please enter minimum number of 3 characters..")
    private String city;

    @NotNull(message = "The state must not be empty")
    @Size(max = 50, message = "state is too long. Please enter maximum number of 50 characters.")
    @Size(min = 3, message = "state is too short. Please enter minimum number of 3 characters..")
    private String state;

    @NotNull(message = "The country must not be empty")
    @Size(max = 50, message = "country is too long. Please enter maximum number of 50 characters.")
    @Size(min = 3, message = "country is too short. Please enter minimum number of 3 characters..")
    private String country;

}
