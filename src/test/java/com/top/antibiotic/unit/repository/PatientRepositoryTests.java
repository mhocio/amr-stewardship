package com.top.antibiotic.unit.repository;

import com.top.antibiotic.entities.Patient;
import com.top.antibiotic.repository.PatientRepository;
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
public class PatientRepositoryTests {

    @Autowired
    PatientRepository patientRepository;

    @Test
    public void givenPatientId_whenFindById_thenReturnPatient() {
        // given
        Patient patient = Patient.builder()
                .firstName("Andrzej")
                .secondName("Strzelba")
                .pesel("704100213211")
                .build();
        patientRepository.save(patient);

        // when
        Optional<Patient> foundPatient = patientRepository.findById(1L);

        // then
        Assert.assertEquals(Optional.of(patient), foundPatient);
    }

    @Test
    public void givenPatientIds_whenFindAll_thenReturnPatients() {
        // given
        Patient patient1 = Patient.builder()
                .firstName("Andrzej")
                .secondName("Strzelba")
                .pesel("704100213211")
                .build();
        patientRepository.save(patient1);

        Patient patient2 = Patient.builder()
                .firstName("Jan")
                .secondName("Kowalski")
                .pesel("904100213211")
                .build();
        patientRepository.save(patient2);

        // when
        List<Patient> foundPatients = patientRepository.findAll();

        // then
        Assert.assertEquals(patient1, foundPatients.get(0));
        Assert.assertEquals(patient2, foundPatients.get(1));
    }

    @Test
    public void givenPatientArbitraryId_whenFindById_thenDontReturnPatient() {
        // given
        Patient patient = Patient.builder()
                .patientId(999L)
                .firstName("Andrzej")
                .secondName("Strzelba")
                .pesel("704100213211")
                .build();
        patientRepository.save(patient);

        // when
        Optional<Patient> foundPatient = patientRepository.findById(999L);

        // then
        Assert.assertEquals(Optional.empty(), foundPatient);
    }
}