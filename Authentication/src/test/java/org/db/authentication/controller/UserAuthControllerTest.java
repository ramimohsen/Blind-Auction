package org.db.authentication.controller;

import org.db.authentication.dto.UserSignUpRequest;
import org.db.authentication.exception.custom.UserAlreadyExistException;
import org.db.authentication.service.UserAuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserAuthService userAuthService;

    @Test
    void testRegisterUser() throws Exception {
        mockMvc.perform(post("/api/user/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"testUser@gmail.com\", \"password\":\"testPassword\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testAuthenticate() throws Exception {
        mockMvc.perform(post("/api/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"testUser@gmail.com\", \"password\":\"testPassword\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testRegisterUserAlreadyExists() throws Exception {
        Mockito.when(userAuthService.registerUser(any(UserSignUpRequest.class)))
                .thenThrow(new UserAlreadyExistException("User already exists"));

        mockMvc.perform(post("/api/user/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"testUser@gmail.com\", \"password\":\"testPassword\"}"))
                .andExpect(status().isBadRequest());
    }

}