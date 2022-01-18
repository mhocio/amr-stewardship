package com.top.antibiotic.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.top.antibiotic.dto.MaterialDto;
import com.top.antibiotic.dto.PatientDto;
import com.top.antibiotic.entities.Material;
import com.top.antibiotic.entities.Patient;
import com.top.antibiotic.mapper.MaterialMapper;
import com.top.antibiotic.mapper.PatientMapper;
import com.top.antibiotic.servcice.MaterialService;
import com.top.antibiotic.servcice.PatientService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;

@ActiveProfiles("tests")
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
public class MaterialIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private MaterialMapper materialMapper;

    private final String uri = "/api/material";
    private final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Test
    public void addMaterialAndGetAllTest() throws Exception {
        String name = "Material name";
        materialService.save(MaterialDto.builder()
                .name(name)
                .build()
        );

        mvc.perform(get(uri))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].materialId", is(1)))
                .andExpect(jsonPath("$[0].name", is(name)));

        String name2 = "Material name 2";
        materialService.save(MaterialDto.builder()
                .name(name2)
                .build()
        );

        mvc.perform(get(uri))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].materialId", is(2)))
                .andExpect(jsonPath("$[1].name", is(name2)));

        mvc.perform(get(uri + "/2"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.materialId", is(2)))
                .andExpect(jsonPath("$.name", is(name2)));
    }

    @Test
    public void addWrongDtoTest() throws Exception {
        String json = ow.writeValueAsString(
                materialMapper.mapMaterialToDto(Material.builder().
                        materialId(42L).build())
        );

        ResultActions r = mvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(handler().handlerType(com.top.antibiotic.controllers.MaterialController.class))
                .andExpect(handler().methodName("createMaterial"));
    }

    @Test
    public void postAndGetTest() throws Exception {
        String name = "Material name";
        String json = ow.writeValueAsString(
                materialMapper.mapMaterialToDto(Material.builder()
                        .materialId(42L)
                        .name(name)
                        .build())
        );

        ResultActions r = mvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.materialId", is(1)))
                .andExpect(jsonPath("$.name", is(name)));

        mvc.perform(get(uri + "/1"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.materialId", is(1)))
                .andExpect(jsonPath("$.name", is(name)));
    }

}