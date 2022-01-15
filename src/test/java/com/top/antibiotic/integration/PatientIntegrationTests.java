package com.top.antibiotic.integration;

import com.top.antibiotic.dto.PatientDto;
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
public class PatientIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PatientService patientService;

    @Test
    public void addPatientAndGetAllTest() throws Exception {
        patientService.save(PatientDto.builder()
                        .firstName("Mikolaj")
                        .secondName("Hoscilo")
                        .pesel("345345")
                        .build()
        );

        mvc.perform(get("/api/patient"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patientId", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("Mikolaj")));
    }
/*    @Test
    public void secondTest() throws Exception {
        ResultActions r = mvc.perform(get("/api/ward/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        int status = r.andReturn().getResponse().getStatus();
        log.info(String.valueOf(status));
    }*/
}

