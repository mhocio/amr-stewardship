package com.top.antibiotic.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.top.antibiotic.dto.WardDto;
import com.top.antibiotic.entities.Ward;
import com.top.antibiotic.mapper.WardMapper;
import com.top.antibiotic.servcice.WardService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("tests dev")
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
public class WardIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WardService wardService;

    @Autowired
    private WardMapper wardMapper;

    private final String uri = "/api/ward";
    private final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Test
    public void AddWardAndGetAllTest() throws Exception {
        String wardName = "Second Name";
        wardService.save(WardDto.builder()
                        .name(wardName)
                        .build()
        );

        mvc.perform(get(uri))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(com.top.antibiotic.controllers.WardController.class))
                .andExpect(handler().methodName("getAllWards"))
                .andExpect(jsonPath("$[0].wardId", is(1)))
                .andExpect(jsonPath("$[0].name", is("Second Name")));
    }

    @Test
    public void AddWardAndGetByIdTest() throws Exception {
        String wardName = "Ward Name";
        wardService.save(WardDto.builder()
                        .name(wardName)
                        .build()
        );

        ResultActions r = mvc.perform(get(uri + "/1"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(com.top.antibiotic.controllers.WardController.class))
                .andExpect(handler().methodName("getWard"))
                .andExpect(jsonPath("$.wardId", is(1)))
                .andExpect(jsonPath("$.name", is("Ward Name")));
    }

    @Test
    public void AddWardAndGetByNameTest() throws Exception {
        String wardName = "Ward Name";
        wardService.save(WardDto.builder()
                .name(wardName)
                .build()
        );

        ResultActions r = mvc.perform(get(uri + "/by-name/" + wardName))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(com.top.antibiotic.controllers.WardController.class))
                .andExpect(handler().methodName("getWard"))
                .andExpect(jsonPath("$.wardId", is(1)))
                .andExpect(jsonPath("$.name", is("Ward Name")));
    }

    @Test
    public void PostWardAndGetTest() throws Exception {
        String wardName = "Ward Name";
        String json = ow.writeValueAsString(
                wardMapper.mapWardToDto(Ward.builder().
                        name(wardName).build())
        );

        ResultActions r = mvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(handler().handlerType(com.top.antibiotic.controllers.WardController.class))
                .andExpect(handler().methodName("createWard"))
                .andExpect(jsonPath("$.wardId", is(1)))
                .andExpect(jsonPath("$.name", is(wardName)));

        mvc.perform(get(uri + "/by-name/" + wardName))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(com.top.antibiotic.controllers.WardController.class))
                .andExpect(handler().methodName("getWard"))
                .andExpect(jsonPath("$.wardId", is(1)))
                .andExpect(jsonPath("$.name", is(wardName)));

        String secondWardName = "Second Name";
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        json = ow.writeValueAsString(
                wardMapper.mapWardToDto(Ward.builder()
                        .name(secondWardName)
                        .wardId(42L)
                        .build())
        );

        mvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(handler().handlerType(com.top.antibiotic.controllers.WardController.class))
                .andExpect(handler().methodName("createWard"))
                .andExpect(jsonPath("$.wardId", is(2)))
                .andExpect(jsonPath("$.name", is(secondWardName)));
    }
}
