package com.top.antibiotic.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.top.antibiotic.dto.FratRequest;
import com.top.antibiotic.entities.Antibiogram;
import com.top.antibiotic.entities.Bacteria;
import com.top.antibiotic.entities.Examination;
import com.top.antibiotic.entities.Ward;
import com.top.antibiotic.exceptions.AntibioticsException;
import com.top.antibiotic.repository.AntibiogramRepository;
import com.top.antibiotic.repository.ExaminationRepository;
import com.top.antibiotic.repository.UserRepository;
import com.top.antibiotic.repository.WardRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles({"tests"})
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
public class AntibiogramTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AntibiogramRepository antibiogramRepository;

    @Autowired
    private ExaminationRepository examinationRepository;

    @Autowired
    private WardRepository wardRepository;

    private final String uri = "/api/antibiogram";
    private final String fratTableUri = "/api/frat-table";
    private final String bacteriaUri = "/api/bacteria";
    private final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Test
    @Transactional
    public void postFratFileTest() throws Exception {
        //For Excel2007 and above .xlsx files - application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
        //For BIFF .xls files - application/vnd.ms-excel
        String fileName = "FRAT.xlsx";
        MockMultipartFile file = new MockMultipartFile("file"
                , fileName, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                , new ClassPathResource(fileName).getInputStream());

        mvc.perform(MockMvcRequestBuilders.multipart(uri + "/import")
                        .file(file))
                .andExpect(status().is(200));

        List<Examination> exams =  examinationRepository.findAll();
        Assert.assertEquals(5, exams.size());
        Examination e1 =  examinationRepository.findByNumber(801819025L).orElseThrow(
                () -> new AntibioticsException("no exam 801819025L found")
        );
        Assert.assertEquals("Wymaz z ropnia - posiew", e1.getMaterial().getName());
        Assert.assertEquals("69071102007", e1.getPatient().getPesel());
        Assert.assertEquals("Chirurgia", e1.getWard().getName());
        Assert.assertEquals(801819025L, e1.getNumber().longValue());

        Ward ward = wardRepository.findByName("Chirurgia").orElseThrow(
                () -> new AntibioticsException("no ward Chirurgia found")
        );

        Assert.assertEquals(61, antibiogramRepository.findByWard(ward).size());

        Antibiogram antibiogram = antibiogramRepository.getById(1L);
        Assert.assertEquals("Chirurgia", antibiogram.getWard().getName());
        Assert.assertEquals("Kowal", antibiogram.getPatient().getFirstName());

        Assert.assertEquals("Joanna", antibiogram.getPatient().getSecondName());
        Assert.assertEquals("69071102007", antibiogram.getPatient().getPesel());
        Assert.assertEquals("801819025", antibiogram.getOrderNumber().toString());
        Assert.assertEquals("Wymaz z ropnia - posiew", antibiogram.getMaterial().getName());
        Assert.assertEquals("Staphylococcus aureus", antibiogram.getBacteria().getName());
        Assert.assertEquals("", antibiogram.getBacteria().getSubtype());
        Assert.assertEquals("R", antibiogram.getSusceptibility());
        Assert.assertEquals("<=2", antibiogram.getMic());
        Assert.assertFalse(antibiogram.isAlert());
        Assert.assertTrue(antibiogram.isPatogen());
        Assert.assertEquals("obfity", antibiogram.getGrowth());
        Assert.assertTrue(antibiogram.isFirstIsolate());
        Assert.assertFalse(antibiogram.isHospitalInfection());
        Assert.assertEquals("308323", antibiogram.getOrderId().toString());
        Assert.assertEquals("282824", antibiogram.getTestId().toString());
        Assert.assertEquals("290337", antibiogram.getIsolationId().toString());
        Assert.assertEquals("MAU", antibiogram.getIsolationCode());
        Assert.assertEquals("1", antibiogram.getIsolationNum().toString());
        Assert.assertEquals("AN", antibiogram.getAntibiotic().getCode());
        Assert.assertEquals("oporny", antibiogram.getResult());
        Assert.assertEquals("042.0128", antibiogram.getDailyNumber());
        Assert.assertEquals("R", antibiogram.getMode());
        Assert.assertEquals("", antibiogram.getPryw());

        MvcResult result = mvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(com.top.antibiotic.controllers.AntibiogramController.class))
                .andExpect(handler().methodName("getAllAntibiograms"))
                .andReturn();

        result = mvc.perform(get(uri + "/by-pesel/69071102007"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(com.top.antibiotic.controllers.AntibiogramController.class))
                .andExpect(handler().methodName("getAllByPesel"))
                .andReturn();
    }

    @Test
    @Transactional
    public void postSmallerFileTest() throws Exception {
        //For Excel2007 and above .xlsx files - application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
        //For BIFF .xls files - application/vnd.ms-excel
        String fileName = "smaller.xlsx";
        MockMultipartFile file = new MockMultipartFile("file"
                , fileName, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                , new ClassPathResource(fileName).getInputStream());

        mvc.perform(MockMvcRequestBuilders.multipart(uri + "/import")
                        .file(file))
                .andExpect(status().is(200));

        List<Examination> exams =  examinationRepository.findAll();
        Assert.assertEquals(400, exams.size());

        mvc.perform(get(fratTableUri + "/Chirurgia/M1"))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    @Transactional
    public void postSmaller2FileTest() throws Exception {
        //For Excel2007 and above .xlsx files - application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
        //For BIFF .xls files - application/vnd.ms-excel
        String fileName = "smaller2.xlsx";
        MockMultipartFile file = new MockMultipartFile("file"
                , fileName, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                , new ClassPathResource(fileName).getInputStream());

        mvc.perform(MockMvcRequestBuilders.multipart(uri + "/import")
                        .file(file))
                .andExpect(status().is(200));

        List<Examination> exams =  examinationRepository.findAll();
        Assert.assertEquals(169, exams.size());

        List<Antibiogram> antibiograms = antibiogramRepository.findAll();
        Assert.assertEquals(1280, antibiograms.size());

        mvc.perform(get(fratTableUri + "/Chirurgia/Wymaz z ropnia - posiew"))
                .andDo(print())
                .andExpect(status().is(200));
    }
}
