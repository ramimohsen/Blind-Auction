package org.db.authentication.dto;


import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserSignUpResponse {

    private String email;

    private boolean success;
}
