package com.top.antibiotic.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jayway.jsonpath.JsonPath;
import com.top.antibiotic.dto.BacteriaDto;
import com.top.antibiotic.dto.LoginRequest;
import com.top.antibiotic.dto.RefreshTokenRequest;
import com.top.antibiotic.dto.RegisterRequest;
import com.top.antibiotic.entities.Bacteria;
import com.top.antibiotic.mapper.BacteriaMapper;
import com.top.antibiotic.repository.UserRepository;
import com.top.antibiotic.servcice.AuthService;
import com.top.antibiotic.servcice.BacteriaService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles({"tests", "prod"})
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
public class AuthIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    private final String uri = "/api/auth";
    private final String bacteriaUri = "/api/bacteria";
    private final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Test
    public void userExistsTest() throws Exception {
        String email = "email@mail.com";
        String password = "password";
        String username = "username";

        // REGISTER
        String json = ow.writeValueAsString(
                RegisterRequest.builder()
                        .email(email)
                        .password(password)
                        .username(username)
                        .build()
        );
        mvc.perform(post(uri + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().string("User Registration Successful"));

        String email2 = "email@mail.com";
        String password2 = "password";
        String username2 = "user2";

        // REGISTER
        String json2 = ow.writeValueAsString(
                RegisterRequest.builder()
                        .email(email2)
                        .password(password2)
                        .username(username2)
                        .build()
        );
        mvc.perform(post(uri + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json2))
                .andDo(print())
                .andExpect(status().is(409))
                .andExpect(content().string("User already exists with email: email@mail.com"));

        String email3 = "other_mail@mail.com";
        String password3 = "password";
        String username3 = "username";

        // REGISTER
        String json3 = ow.writeValueAsString(
                RegisterRequest.builder()
                        .email(email3)
                        .password(password3)
                        .username(username3)
                        .build()
        );
        mvc.perform(post(uri + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json3))
                .andDo(print())
                .andExpect(status().is(409))
                .andExpect(content().string("User already exists with username: username"));
    }

    @Test
    public void loginFailedTest() throws Exception {
        String email = "email@mail.com";
        String password = "password";
        String username = "username";

        // REGISTER
        String json = ow.writeValueAsString(
                RegisterRequest.builder()
                        .email(email)
                        .password(password)
                        .username(username)
                        .build()
        );
        mvc.perform(post(uri + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().string("User Registration Successful"));

        json = ow.writeValueAsString(
                LoginRequest.builder()
                        .password(password)
                        .username("some_username")
                        .build()
        );
        MvcResult loginResult = mvc.perform(post(uri + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is(403))
                .andReturn();

        json = ow.writeValueAsString(
                LoginRequest.builder()
                        .password("wrong password")
                        .username(username)
                        .build()
        );
        loginResult = mvc.perform(post(uri + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is(403))
                .andReturn();
    }

    @Test
    public void postSignupTest() throws Exception {
        String email = "email@mail.com";
        String password = "password";
        String username = "username";

        // REGISTER
        String json = ow.writeValueAsString(
                RegisterRequest.builder()
                        .email(email)
                        .password(password)
                        .username(username)
                        .build()
        );
        mvc.perform(post(uri + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().string("User Registration Successful"));

        // LOGIN
        json = ow.writeValueAsString(
                LoginRequest.builder()
                        .password(password)
                        .username(username)
                        .build()
        );
        MvcResult loginResult = mvc.perform(post(uri + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.username", is(username)))
                .andReturn();

        String response = loginResult.getResponse().getContentAsString();
        String authenticationToken = JsonPath.parse(response)
                .read("$.authenticationToken");
        String refreshToken = JsonPath.parse(response)
                .read("$.refreshToken");

        // GET bacterias
        mvc.perform(get(bacteriaUri)
                        .header("Authorization",
                                "Bearer " + authenticationToken))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(com.top.antibiotic.controllers.BacteriaController.class))
                .andExpect(handler().methodName("getAllBacterias"));

        // wait for JWT to expire
        TimeUnit.SECONDS.sleep(2);
        mvc.perform(get(bacteriaUri)
                        .header("Authorization",
                                "Bearer " + authenticationToken))
                .andDo(print())
                .andExpect(status().is(401));
        mvc.perform(get(bacteriaUri))
                .andDo(print())
                .andExpect(status().is(403));

        // expired - renew the JWT
        json = ow.writeValueAsString(
                RefreshTokenRequest.builder()
                        .username(username)
                        .refreshToken(refreshToken)
                        .build()
        );
        MvcResult refreshTokenResult = mvc.perform(post(uri + "/refresh/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.username", is(username)))
                .andReturn();

        response = refreshTokenResult.getResponse().getContentAsString();
        authenticationToken = JsonPath.parse(response)
                .read("$.authenticationToken");
        refreshToken = JsonPath.parse(response)
                .read("$.refreshToken");

        // GET bacterias
        mvc.perform(get(bacteriaUri)
                        .header("Authorization",
                                "Bearer " + authenticationToken))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(com.top.antibiotic.controllers.BacteriaController.class))
                .andExpect(handler().methodName("getAllBacterias"));

        // LOGOUT
        json = ow.writeValueAsString(
                RefreshTokenRequest.builder()
                        .username(username)
                        .refreshToken(refreshToken)
                        .build()
        );
        mvc.perform(post(uri + "/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();

        // try to refresh JWT
        json = ow.writeValueAsString(
                RefreshTokenRequest.builder()
                        .username(username)
                        .refreshToken(refreshToken)
                        .build()
        );
        mvc.perform(post(uri + "/refresh/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is(403));
    }

}