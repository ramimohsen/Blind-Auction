package org.db.authentication.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSignUpRequest {

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotNull
    @Size(min = 6, max = 30)
    private String password;

    @NotNull
    private UserType userType;
}
