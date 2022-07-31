package com.top.antibiotic.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ActiveProfiles({"tests", "dev", "synchronous-tests"})
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
public class ChartIntegrationTests {

    @Autowired
    private MockMvc mvc;

    private final String uri = "/api/chart";
    private final String importAntibiogramsUri = "/api/antibiogram/import/asseco";
    private final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Test
    @Transactional
    public void testFratChartFromSmallesTfile() throws Exception {
        String fileName = "FRAT.xlsx";
        MockMultipartFile file = new MockMultipartFile("file"
                , fileName, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                , new ClassPathResource(fileName).getInputStream());

        mvc.perform(MockMvcRequestBuilders.multipart(importAntibiogramsUri)
                        .file(file))
                .andExpect(status().is(200));

        MvcResult result = mvc.perform(get(uri +
                "/FRAT/Chirurgia/Wymaz z ropnia - posiew/2017/2019"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(com.top.antibiotic.controllers.ChartController.class))
                .andExpect(handler().methodName("getChartFRATData"))
                .andReturn();

        String response = result.getResponse().getContentAsString();

        Assert.assertEquals("[\"Nazwa\",\"2017\",\"2018\",\"2019\"]",
                JsonPath.parse((response)).read("$.barChart.keys").toString());

        Assert.assertEquals("[\"Linezolid\",\"0\",\"66.66666666666667\",\"0\"]",
                JsonPath.parse((response)).read("$.barChart.results[0]").toString());

        Assert.assertEquals("[\"Nazwa\",\"Amikacin\",\"Ciprofloxacin\",\"Clindamycin\",\"Daptomycin\",\"Erythromycin\",\"Gentamicin\",\"Levofloxacin\",\"Linezolid\",\"Oxacillin\",\"Rifampicin\",\"Teicoplanin\",\"Tetracycline\",\"Tigecycline\",\"Trimethoprim\\/Sulfamethoxazole\",\"Vancomycin\",\"Cefoxitin\",\"Ampicillin\",\"Fosfomycin\",\"Gentamicin High Level (synergy)\",\"Imipenem\",\"Nitrofurantoin\",\"Streptomycin High Level (synergy)\",\"Quinupristin\\/Dalfopristin\",\"Amoxicillin\\/Clavulanic Acid\",\"Metronidazole\",\"Penicilline G\"]",
                JsonPath.parse((response)).read("$.graphChart.keys").toString());

        Assert.assertEquals("[\"2018\",\"0.0\",\"33.333333333333336\",\"66.66666666666667\",\"66.66666666666667\",\"33.333333333333336\",\"66.66666666666667\",\"33.333333333333336\",\"66.66666666666667\",\"33.333333333333336\",\"66.66666666666667\",\"33.333333333333336\",\"66.66666666666667\",\"66.66666666666667\",\"66.66666666666667\",\"100.0\",\"0\",\"0\",\"0\",\"0\",\"33.333333333333336\",\"0\",\"0\",\"0\",\"33.333333333333336\",\"0.0\",\"33.333333333333336\"]",
                JsonPath.parse((response)).read("$.graphChart.results[1]").toString());
    }

    @Test
    @Transactional
    public void testSusceptibilityChartFromSmallesTfile() throws Exception {
        String fileName = "FRAT.xlsx";
        MockMultipartFile file = new MockMultipartFile("file"
                , fileName, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                , new ClassPathResource(fileName).getInputStream());

        mvc.perform(MockMvcRequestBuilders.multipart(importAntibiogramsUri)
                        .file(file))
                .andExpect(status().is(200));

        MvcResult result = mvc.perform(get(uri +
                        "/Staphylococcus epidermidis/2018/2021"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(com.top.antibiotic.controllers.ChartController.class))
                .andExpect(handler().methodName("getChartData"))
                .andReturn();
        log.info(result.getResponse().getContentAsString());

        String response = result.getResponse().getContentAsString();

        Assert.assertEquals("[\"Nazwa\",\"2018\",\"2019\",\"2020\",\"2021\"]",
                JsonPath.parse((response)).read("$.keys").toString());

        Assert.assertEquals("[\"Linezolid\",\"100.00\",\"0\",\"0\",\"0\"]",
                JsonPath.parse((response)).read("$.results[0]").toString());
    }
}
