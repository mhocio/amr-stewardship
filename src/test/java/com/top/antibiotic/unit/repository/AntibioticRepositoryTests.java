package com.top.antibiotic.unit.repository;

import com.top.antibiotic.entities.Antibiotic;
import com.top.antibiotic.repository.AntibioticRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
public class AntibioticRepositoryTests {

    @Autowired
    AntibioticRepository antibioticRepository;

    @Test
    public void givenAntibioticName_whenFindByAntibioticName_thenReturnAntibiotic() {
        // given
        String antibioticName = "antibiotic test name";
        antibioticRepository.save(Antibiotic.builder().name(antibioticName).build());

        // when
        Optional<Antibiotic> foundAntibiotic = antibioticRepository.findByName(antibioticName);

        // then
        Assert.assertEquals(antibioticName, foundAntibiotic.get().getName());
    }

    @Test
    public void givenAntibioticName_whenFindById_thenReturnAntibiotic() {
        // given
        Long id = 1L;
        String antibioticName = "antibiotic test name";
        antibioticRepository.save(Antibiotic.builder().name(antibioticName).build());

        // when
        Optional<Antibiotic> foundAntibiotic = antibioticRepository.findById(id);

        // then
        Assert.assertEquals(id, foundAntibiotic.get().getAntibioticId());
    }

    @Test
    public void givenArbitraryAntibioticId_whenFindById_thenDontReturnAntibiotic() {
        // given
        Long id = 999L;
        String antibioticName = "antibiotic test name";
        antibioticRepository.save(Antibiotic.builder().name(antibioticName).antibioticId(id).build());

        // when
        boolean exists999 = antibioticRepository.existsById(id);
        boolean exists1 = antibioticRepository.existsById(1L);

        // then
        Assert.assertFalse(exists999);
        Assert.assertTrue(exists1);
    }

    @Test
    public void givenAntibioticNames_whenFindAll_thenReturnAntibiotics() {
        // given
        String antibioticName1 = "antibiotic test name1";
        String antibioticName2 = "antibiotic test name2";

        antibioticRepository.save(Antibiotic.builder().name(antibioticName1).build());
        antibioticRepository.save(Antibiotic.builder().name(antibioticName2).build());

        // when
        List<Antibiotic> foundAntibiotics = antibioticRepository.findAll();

        // then
        Assert.assertEquals(Optional.of(1L), Optional.ofNullable(foundAntibiotics.get(0).getAntibioticId()));
        Assert.assertEquals(Optional.of(2L), Optional.ofNullable(foundAntibiotics.get(1).getAntibioticId()));

    }


}
