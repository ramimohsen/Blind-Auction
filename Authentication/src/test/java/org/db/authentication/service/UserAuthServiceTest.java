package org.db.authentication.service;

import org.db.authentication.dto.JwtResponse;
import org.db.authentication.dto.UserLoginRequest;
import org.db.authentication.dto.UserSignUpRequest;
import org.db.authentication.dto.UserSignUpResponse;
import org.db.authentication.exception.custom.UserAlreadyExistException;
import org.db.authentication.model.User;
import org.db.authentication.repository.UserRepository;
import org.db.authentication.service.security.JwtUtils;
import org.db.authentication.service.security.UserDetailsImpl;
import org.db.authentication.service.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;


@SpringBootTest
class UserAuthServiceTest {

    @Autowired
    private UserAuthService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;


    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void when_register_new_user_is_successful() throws UserAlreadyExistException {

        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");

        User savedUser = User.builder()
                .email("test@example.com")
                .password("hashedPassword")
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserSignUpRequest userSignUpRequest = new UserSignUpRequest();
        userSignUpRequest.setEmail("test@example.com");
        userSignUpRequest.setPassword("password");

        UserSignUpResponse response = userService.registerUser(userSignUpRequest);

        verify(userRepository, times(1)).save(any());

        Assertions.assertEquals("test@example.com", response.getEmail());
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    void when_register_already_existing_user_throws() {

        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        UserSignUpRequest userSignUpRequest = new UserSignUpRequest();
        userSignUpRequest.setEmail("test@example.com");

        Assertions.assertThrows(UserAlreadyExistException.class, () -> userService.registerUser(userSignUpRequest));
        verify(userRepository, times(1)).existsByEmail("test@example.com");
    }

    @Test
    void when_authenticating_user_is_successful() {

        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setEmail("test@example.com");
        userLoginRequest.setPassword("password");

        UserDetails userDetails = UserDetailsImpl.builder()
                .email("test@example.com").password("hashed").build();

        when(passwordEncoder.encode(anyString())).thenReturn("hashed");

        when(userRepository.findUserByEmail("test@example.com"))
                .thenReturn(Optional.ofNullable(User.builder().email("test@example.com")
                        .password("hashed")
                        .build()));

        when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(userDetails);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        when(jwtUtils.generateJwtToken(authentication)).thenReturn("mocked.jwt.token");

        JwtResponse jwtResponse = userService.authenticate(userLoginRequest);

        Assertions.assertEquals("mocked.jwt.token", jwtResponse.getToken());
        Assertions.assertEquals("test@example.com", jwtResponse.getEmail());
    }

    @Test
    void when_authenticating_user_that_does_not_exist_throws() {

        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setEmail("nonexistent@example.com");

        when(userDetailsService.loadUserByUsername("nonexistent@example.com"))
                .thenThrow(new UsernameNotFoundException("User not found"));

        when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);

        Assertions.assertThrows(BadCredentialsException.class, () ->
                userService.authenticate(userLoginRequest));
    }
}
