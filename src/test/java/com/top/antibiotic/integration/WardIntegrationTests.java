package com.top.antibiotic.integration;

import com.top.antibiotic.dto.WardDto;
import com.top.antibiotic.servcice.WardService;
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
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("tests")
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

    @Test
    public void AddPatientAndGetAllTest() throws Exception {
        String wardName = "Second Name";
        wardService.save(WardDto.builder()
                        .name(wardName)
                        .build()
        );

        mvc.perform(get("/api/ward"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(com.top.antibiotic.controllers.WardController.class))
                .andExpect(handler().methodName("getAllWards"))
                .andExpect(jsonPath("$[0].wardId", is(1)))
                .andExpect(jsonPath("$[0].name", is("Second Name")));
    }

    @Test
    public void AddPatientAndGetByIdTest() throws Exception {
        String wardName = "Ward Name";
        wardService.save(WardDto.builder()
                        .name(wardName)
                        .build()
        );

        mvc.perform(get("/api/ward"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(com.top.antibiotic.controllers.WardController.class))
                .andExpect(handler().methodName("getAllWards"))
                .andExpect(jsonPath("$[0].wardId", is(1)))
                .andExpect(jsonPath("$[0].name", is("Ward Name")));

        ResultActions r = mvc.perform(get("/api/ward/1"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(com.top.antibiotic.controllers.WardController.class))
                .andExpect(handler().methodName("getWard"))
                .andExpect(jsonPath("$.wardId", is(1)))
                .andExpect(jsonPath("$.name", is("Ward Name")));
    }
}
