package org.db.authentication.dto;


import lombok.*;

@Getter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateTokenResponse {

    private Boolean valid;

    private String userType;

}
