package com.top.antibiotic.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.top.antibiotic.dto.AntibioticDto;
import com.top.antibiotic.entities.Antibiotic;
import com.top.antibiotic.entities.Patient;
import com.top.antibiotic.mapper.AntibioticMapper;
import com.top.antibiotic.repository.AntibioticRepository;
import com.top.antibiotic.servcice.AntibioticService;
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
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles({"tests", "dev"})
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
public class AntibioticIngegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AntibioticService antibioticService;

    @Autowired
    private AntibioticMapper antibioticMapper;

    private final String uri = "/api/antibiotic";
    private final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Test
    @Transactional
    public void test() throws Exception {
        String name = "A";
        String code = "code1";

        antibioticService.save(AntibioticDto.builder()
                        .antibioticId(42L)
                        .name(name)
                        .code(code)
                        .build());

        mvc.perform(get(uri))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].antibioticId", is(1)))
                .andExpect(jsonPath("$[0].name", is(name)))
                .andExpect(jsonPath("$[0].code", is(code)));

        mvc.perform(get(uri + "/1"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.antibioticId", is(1)))
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.code", is(code)));

        String name2 = "A2";
        String code2 = "code2";
        antibioticService.save(AntibioticDto.builder()
                .antibioticId(42L)
                .name(name2)
                .code(code2)
                .build());

        mvc.perform(get(uri))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].antibioticId", is(2)))
                .andExpect(jsonPath("$[1].name", is(name2)))
                .andExpect(jsonPath("$[1].code", is(code2)));
    }

    @Test
    @Transactional
    public void postAndGetAntibioticTest() throws Exception {
        String name = "A";
        String code = "code1";
        String json = ow.writeValueAsString(
                antibioticMapper.mapAntibioticToDto(Antibiotic.builder()
                        .antibioticId(42L)
                        .name(name)
                        .code(code)
                        .build())
        );

        ResultActions r = mvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.antibioticId", is(1)))
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.code", is(code)))
                .andExpect(handler().handlerType(com.top.antibiotic.controllers.AntibioticController.class))
                .andExpect(handler().methodName("createAntibiotic"));

        mvc.perform(get(uri))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].antibioticId", is(1)))
                .andExpect(jsonPath("$[0].name", is(name)))
                .andExpect(jsonPath("$[0].code", is(code)));
    }
}
