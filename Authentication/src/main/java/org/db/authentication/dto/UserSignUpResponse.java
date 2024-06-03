package org.db.authentication.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSignUpResponse {

    private String email;

    private boolean success;

    private UserType userType;
}
