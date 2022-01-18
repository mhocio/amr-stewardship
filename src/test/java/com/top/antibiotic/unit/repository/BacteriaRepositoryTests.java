package com.top.antibiotic.unit.repository;

import com.top.antibiotic.entities.Bacteria;
import com.top.antibiotic.repository.BacteriaRepository;
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
public class BacteriaRepositoryTests {

    @Autowired
    BacteriaRepository bacteriaRepository;

    @Test
    public void givenBacteriaName_whenFindByBacteriaName_thenReturnBacteria() {
        // given
        String bacteriaName = "bacteria test name";
        bacteriaRepository.save(Bacteria.builder().name(bacteriaName).build());

        // when
        Optional<Bacteria> foundBacteria = bacteriaRepository.findByName(bacteriaName);

        // then
        Assert.assertEquals(bacteriaName, foundBacteria.get().getName());
    }

    @Test
    public void givenBacteriaName_whenFindById_thenReturnBacteria() {
        // given
        Long id = 1L;
        String bacteriaName = "bacteria test name";
        bacteriaRepository.save(Bacteria.builder().name(bacteriaName).build());

        // when
        Optional<Bacteria> foundBacteria = bacteriaRepository.findById(id);

        // then
        Assert.assertEquals(id, foundBacteria.get().getBacteriaId());
    }

    @Test
    public void givenArbitraryBacteriaId_whenFindById_thenDontReturnBacteria() {
        // given
        Long id = 999L;
        String bacteriaName = "bacteria test name";
        bacteriaRepository.save(Bacteria.builder().name(bacteriaName).bacteriaId(id).build());

        // when
        boolean exists999 = bacteriaRepository.existsById(id);
        boolean exists1 = bacteriaRepository.existsById(1L);

        // then
        Assert.assertFalse(exists999);
        Assert.assertTrue(exists1);
    }

    @Test
    public void givenBacteriaNames_whenFindAll_thenReturnBacterias() {
        // given
        String bacteriaName1 = "bacteria test name1";
        String bacteriaName2 = "bacteria test name2";

        bacteriaRepository.save(Bacteria.builder().name(bacteriaName1).build());
        bacteriaRepository.save(Bacteria.builder().name(bacteriaName2).build());

        // when
        List<Bacteria> foundBacterias = bacteriaRepository.findAll();

        // then
        Assert.assertEquals(Optional.of(1L), Optional.ofNullable(foundBacterias.get(0).getBacteriaId()));
        Assert.assertEquals(Optional.of(2L), Optional.ofNullable(foundBacterias.get(1).getBacteriaId()));

    }


}
