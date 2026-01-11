package com.ecom.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequest {

    @NotBlank
    private String firstName;
    private String lastName;
    @Size(min = 8)
    private String password;
    @Email
    @NotBlank
    private String email;
    private String phone;
    private AddressDTO address;
}
