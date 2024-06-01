package org.db.authentication.service;

import org.db.authentication.dto.JwtResponse;
import org.db.authentication.dto.UserLoginRequest;
import org.db.authentication.dto.UserSignUpRequest;
import org.db.authentication.dto.UserSignUpResponse;
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

}
