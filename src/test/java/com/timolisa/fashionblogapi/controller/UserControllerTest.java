package com.timolisa.fashionblogapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timolisa.fashionblogapi.dto.request.UserLoginDTO;
import com.timolisa.fashionblogapi.dto.response.UserResponseDTO;
import com.timolisa.fashionblogapi.dto.request.UserSignupDTO;
import com.timolisa.fashionblogapi.entity.APIResponse;
import com.timolisa.fashionblogapi.service.UserService;
import com.timolisa.fashionblogapi.utils.ResponseManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.timolisa.fashionblogapi.UserData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private ResponseManager<UserResponseDTO> responseManager;

    @Test
    public void testRegistrationSuccess() throws Exception {
        UserSignupDTO userSignupDTO = buildUserSignUpDTO();
        UserResponseDTO userResponseDTO = buildUserResponseDTO();

        APIResponse<UserResponseDTO> apiResponse =
                new APIResponse<>("Registration successful", true, userResponseDTO);

        when(userService.registerUser(userSignupDTO)).thenReturn(apiResponse);

        when(responseManager.success(userResponseDTO))
                .thenReturn(new APIResponse<>("Registration successful", true, userResponseDTO));

        when(responseManager.error("Username already exists", false))
                .thenReturn(new APIResponse<>("Username already exists", false, null));


        mockMvc.perform(post("/api/fashion-blog/users/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSignupDTO))) // set the request body
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Registration successful"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isNotEmpty());

        MvcResult mvcResult = mockMvc.perform(post("/api/fashion-blog/users/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSignupDTO)))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        APIResponse<UserResponseDTO> actualResponse =
                objectMapper.readValue(responseContent, new TypeReference<>() {
                });

        assertEquals(apiResponse.getPayload().getUsername()
                ,actualResponse.getPayload().getUsername());
    }

    @Test
    public void testLoginSuccess() throws Exception {
        UserLoginDTO loginDTO = buildUserLoginDTO();
        UserResponseDTO userResponseDTO = buildUserResponseDTO();

        APIResponse<UserResponseDTO> apiResponse =
                new APIResponse<>("Login successful", true, userResponseDTO);

        when(userService.loginUser(loginDTO)).thenReturn(apiResponse);

        when(responseManager.success(userResponseDTO))
                .thenReturn(new APIResponse<>("Login successful", true, userResponseDTO));
        mockMvc.perform(post("/api/fashion-blog/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO))) // set the request body
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }
}