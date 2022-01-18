package com.top.antibiotic.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.top.antibiotic.dto.BacteriaDto;
import com.top.antibiotic.dto.MaterialDto;
import com.top.antibiotic.entities.Bacteria;
import com.top.antibiotic.entities.Material;
import com.top.antibiotic.mapper.BacteriaMapper;
import com.top.antibiotic.mapper.MaterialMapper;
import com.top.antibiotic.servcice.BacteriaService;
import com.top.antibiotic.servcice.MaterialService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("tests")
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
public class BacteriaIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BacteriaService bacteriaService;

    @Autowired
    private BacteriaMapper bacteriaMapper;

    private final String uri = "/api/bacteria";
    private final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Test
    public void addBacteriaAndGetAllTest() throws Exception {
        String name = "Bacteria name";
        String subtype = "TYPE";
        bacteriaService.save(BacteriaDto.builder()
                .name(name)
                .subtype(subtype)
                .build()
        );

        mvc.perform(get(uri))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bacteriaId", is(1)))
                .andExpect(jsonPath("$[0].name", is(name)))
                .andExpect(jsonPath("$[0].subtype", is(subtype)));

        String name2 = "Bacteria name 2";
        String subtype2 = "TYPE 2";
        bacteriaService.save(BacteriaDto.builder()
                .name(name2)
                .subtype(subtype2)
                .build()
        );

        mvc.perform(get(uri))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].bacteriaId", is(2)))
                .andExpect(jsonPath("$[1].name", is(name2)))
                .andExpect(jsonPath("$[1].subtype", is(subtype2)));

        mvc.perform(get(uri + "/2"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bacteriaId", is(2)))
                .andExpect(jsonPath("$.name", is(name2)))
                .andExpect(jsonPath("$.subtype", is(subtype2)));
    }

    @Test
    public void addWrongDtoTest() throws Exception {
        String json = ow.writeValueAsString(
                bacteriaMapper.mapBacteriaToDto(Bacteria.builder().
                        bacteriaId(42L).build())
        );

        ResultActions r = mvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(handler().handlerType(com.top.antibiotic.controllers.BacteriaController.class))
                .andExpect(handler().methodName("createBacteria"));
    }

    @Test
    public void postAndGetTest() throws Exception {
        String name = "Bacteria name";
        String subtype = "TYPE";
        String json = ow.writeValueAsString(
                bacteriaMapper.mapBacteriaToDto(Bacteria.builder()
                        .bacteriaId(42L)
                        .name(name)
                        .subtype(subtype)
                        .build())
        );

        ResultActions r = mvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.bacteriaId", is(1)))
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.subtype", is(subtype)));

        mvc.perform(get(uri))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bacteriaId", is(1)))
                .andExpect(jsonPath("$[0].name", is(name)))
                .andExpect(jsonPath("$[0].subtype", is(subtype)));
    }

}