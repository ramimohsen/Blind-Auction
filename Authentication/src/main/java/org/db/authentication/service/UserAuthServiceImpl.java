package org.db.authentication.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.db.authentication.dto.JwtResponse;
import org.db.authentication.dto.UserLoginRequest;
import org.db.authentication.dto.UserSignUpRequest;
import org.db.authentication.dto.UserSignUpResponse;
import org.db.authentication.exception.custom.UserAlreadyExistException;
import org.db.authentication.model.User;
import org.db.authentication.repository.UserRepository;
import org.db.authentication.service.security.JwtUtils;
import org.db.authentication.service.security.UserDetailsImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserAuthServiceImpl implements UserAuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public UserSignUpResponse registerUser(UserSignUpRequest userSignUpRequest) throws UserAlreadyExistException {

        if (Boolean.TRUE.equals(userRepository.existsByEmail(userSignUpRequest.getEmail())))
            throw new UserAlreadyExistException(String.format("User with email %s already exists", userSignUpRequest.getEmail()));

        try {
            User user = userRepository.save(User.builder()
                    .email(userSignUpRequest.getEmail())
                    .registrationDate(LocalDateTime.now())
                    .password(encoder.encode(userSignUpRequest.getPassword()))
                    .build());
            return UserSignUpResponse.builder()
                    .email(user.getEmail())
                    .success(true).build();
        } catch (Exception e) {
            log.error("Error occurred while saving user: {}", e.getMessage());
            return UserSignUpResponse.builder()
                    .email(userSignUpRequest.getEmail())
                    .success(false).build();
        }
    }

    @Override
    public JwtResponse authenticate(UserLoginRequest userLoginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginRequest.getEmail(), userLoginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();


        return JwtResponse.builder()
                .token(jwt).email(userDetails.getEmail())
                .build();
    }

}
