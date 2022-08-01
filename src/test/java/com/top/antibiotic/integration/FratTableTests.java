package com.top.antibiotic.integration;

import com.top.antibiotic.dto.FratRequest;
import com.top.antibiotic.dto.FratTableResponse;
import com.top.antibiotic.dto.SeveralFratTablesResponse;
import com.top.antibiotic.entities.Antibiogram;
import com.top.antibiotic.entities.Examination;
import com.top.antibiotic.repository.AntibiogramRepository;
import com.top.antibiotic.repository.ExaminationRepository;
import com.top.antibiotic.repository.WardRepository;
import com.top.antibiotic.servcice.FratTableService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles({"tests", "synchronous-tests"})
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
public class FratTableTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private FratTableService fratTableService;

    private final String antibiogramUri = "/api/antibiogram";

    @Test
    @Transactional
    public void testSmaller2Sorting() throws Exception {
        //For Excel2007 and above .xlsx files - application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
        //For BIFF .xls files - application/vnd.ms-excel
        String fileName = "smaller2.xlsx";
        MockMultipartFile file = new MockMultipartFile("file"
                , fileName, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                , new ClassPathResource(fileName).getInputStream());

        // import data, use controller endpoint
        mvc.perform(MockMvcRequestBuilders.multipart(antibiogramUri + "/import/asseco")
                        .file(file))
                .andExpect(status().is(200));

        testSorting("Chirurgia", "Wymaz z nosa w kierunku MRSA - posiew",
                10, 4, 165);
        testSorting("Chirurgia", "Wymaz z ropnia - posiew",
                11, 2, 220);
    }

    @Test
    @Transactional
    public void testFratCgmSorting() throws Exception {
        String fileName = "FRAT.xlsx";
        MockMultipartFile file = new MockMultipartFile("file"
                , fileName, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                , new ClassPathResource(fileName).getInputStream());

        mvc.perform(MockMvcRequestBuilders.multipart(antibiogramUri + "/import/cgm")
                        .file(file))
                .andExpect(status().is(200));

        testSorting("Interna i Kardiologia", "Plwocina I", 12, 3, 286);
    }

    void testSorting(String wardName, String materialName, long antibioNo, long bacteriaNo, long antibioCombinationsNo) {
        SeveralFratTablesResponse sortedResponse = fratTableService.getTable(FratRequest.builder()
                .ward(wardName)
                .material(materialName).build());
        SeveralFratTablesResponse unsortedResponse = fratTableService.getTable(FratRequest.builder()
                .ward(wardName)
                .material(materialName).build(), true, false);

        // check if one is not sorted
        Assert.assertNotEquals(sortedResponse, unsortedResponse);

        Assert.assertEquals(sortedResponse.getFirstTable().getAntibiotics().size(), antibioNo);
        Assert.assertEquals(sortedResponse.getFirstTable().getBacterias().size(), bacteriaNo);
        Assert.assertEquals(unsortedResponse.getFirstTable().getAntibiotics().size(), antibioNo);
        Assert.assertEquals(unsortedResponse.getFirstTable().getBacterias().size(), bacteriaNo);
        // 12 choose 2 + 12 choose 3 as combinations of 2 and 3 antibiotics
        Assert.assertEquals(sortedResponse.getSecondTable().getAntibiotics().size(), antibioCombinationsNo);
        Assert.assertEquals(sortedResponse.getSecondTable().getBacterias().size(), bacteriaNo);
        Assert.assertEquals(unsortedResponse.getSecondTable().getAntibiotics().size(), antibioCombinationsNo);
        Assert.assertEquals(unsortedResponse.getSecondTable().getBacterias().size(), bacteriaNo);

        compareBothTables(sortedResponse, unsortedResponse);
    }

    private void compareBothTables(SeveralFratTablesResponse unsorted, SeveralFratTablesResponse sorted) {
        compareTables(unsorted.getFirstTable(), sorted.getFirstTable());
        compareTables(unsorted.getSecondTable(), sorted.getSecondTable());
    }

    private void compareTables(FratTableResponse a, FratTableResponse b) {
        HashMap<String, String> sortedDataFirstTableMap = new HashMap<>();
        for (List<String> row : a.getRows()) {
            String bacteriaName = row.get(0);
            for (int i = 0; i < a.getAntibiotics().size(); i++) {
                String antibioticName = a.getAntibiotics().get(i);
                sortedDataFirstTableMap.put(bacteriaName + antibioticName, row.get(2 + 2*i) + row.get(3 + 2*i));
            }
        }

        HashMap<String, String> unsortedDataFirstTableMap = new HashMap<>();
        for (List<String> row : b.getRows()) {
            String bacteriaName = row.get(0);
            for (int i = 0; i < b.getAntibiotics().size(); i++) {
                String antibioticName = b.getAntibiotics().get(i);
                unsortedDataFirstTableMap.put(bacteriaName + antibioticName, row.get(2 + 2*i) + row.get(3 + 2*i));
            }
        }

        Assert.assertEquals(sortedDataFirstTableMap, unsortedDataFirstTableMap);
    }
}
