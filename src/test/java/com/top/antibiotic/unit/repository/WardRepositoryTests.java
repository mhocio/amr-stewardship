package com.top.antibiotic.unit.repository;

import com.top.antibiotic.entities.Ward;
import com.top.antibiotic.repository.WardRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
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
public class WardRepositoryTests {

    @Autowired
    WardRepository wardRepository;

    @Test
    public void givenWardName_whenFindByWardName_thenReturnWard() {
        // given
        String wardName = "ward test name";
        wardRepository.save(Ward.builder().name(wardName).build());

        // when
        Optional<Ward> foundWard = wardRepository.findByName(wardName);

        // then
        Assert.assertEquals(wardName, foundWard.get().getName());
    }

    @Test
    public void givenWardName_whenFindById_thenReturnWard() {
        // given
        Long id = 1L;
        String wardName = "ward test name";
        wardRepository.save(Ward.builder().name(wardName).build());

        // when
        Optional<Ward> foundWard = wardRepository.findById(id);

        // then
        Assert.assertEquals(id, foundWard.get().getWardId());
    }

    @Test
    public void givenSameWardNames_whenSaveBoth_throwException() {
        // given
        DataIntegrityViolationException e = null;
        String wardName = "ward test name";
        wardRepository.save(Ward.builder().name(wardName).wardId(1L).build());

        try {
            wardRepository.save(Ward.builder().name(wardName).wardId(2L).build());
        } catch (DataIntegrityViolationException ex) {
            e = ex;
        }

        Assert.assertTrue(e instanceof DataIntegrityViolationException);
    }

    @Test
    public void givenArbitraryWardId_whenFindById_thenDontReturnWard() {
        // given
        Long id = 999L;
        String wardName = "ward test name";
        wardRepository.save(Ward.builder().name(wardName).wardId(id).build());

        // when
        boolean exists999 = wardRepository.existsById(id);
        boolean exists1 = wardRepository.existsById(1L);

        // then
        Assert.assertFalse(exists999);
        Assert.assertTrue(exists1);
    }

    @Test
    public void givenWardNames_whenFindAll_thenReturnWards() {
        // given
        String wardName1 = "ward test name1";
        String wardName2 = "ward test name2";

        wardRepository.save(Ward.builder().name(wardName1).build());
        wardRepository.save(Ward.builder().name(wardName2).build());

        // when
        List<Ward> foundWards = wardRepository.findAll();

        // then
        Assert.assertEquals(Optional.of(1L), Optional.ofNullable(foundWards.get(0).getWardId()));
        Assert.assertEquals(Optional.of(2L), Optional.ofNullable(foundWards.get(1).getWardId()));

    }


}
