package org.db.authentication.service;

import org.db.authentication.dto.*;
import org.db.authentication.exception.custom.UserAlreadyExistException;

public interface UserAuthService {

    /**
     * @param userSignUpRequest user signup request dto
     * @return @{@link UserSignUpResponse}
     * @throws UserAlreadyExistException if user email already exists
     */
    UserSignUpResponse registerUser(UserSignUpRequest userSignUpRequest) throws UserAlreadyExistException;


    /**
     * @param userLoginRequest user login request dto
     * @return @{@link JwtResponse} JWT if successful authentication occurred
     */
    JwtResponse authenticate(UserLoginRequest userLoginRequest);

    /**
     * @param token JWT token
     * @return @{@link ValidateTokenResponse} if token is valid
     */
    ValidateTokenResponse validateToken(String token);
}
