package com.top.antibiotic.unit.repository;

import com.top.antibiotic.entities.*;
import com.top.antibiotic.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
public class ExaminationRepositoryTests {

    @Autowired
    ExaminationRepository examinationRepository;

    @Autowired
    WardRepository wardRepository;

    @Autowired
    MaterialRepository materialRepository;

    @Autowired
    PatientRepository patientRepository;

    @Test
    @Transactional
    public void givenExamination_whenExistsByNumber_thenReturnTrue() throws ParseException {
        // given
        Long number = 1L;
        Material material = Material.builder().name("material name1").createdDate(Instant.now()).build();
        Ward ward = Ward.builder().name("ward name").build();
        Patient patient = Patient.builder()
                .firstName("Andrzej")
                .secondName("Strzelba")
                .pesel("90100213231")
                .createdDate(Instant.now())
                .build();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy");
        Date orderDate = simpleDateFormat.parse("1/23/2018 00:00:00");

        material = materialRepository.save(material);
        ward = wardRepository.save(ward);
        patient = patientRepository.save(patient);

        Examination examination = Examination.builder()
                .number(number)
                .ward(ward)
                .material(material)
                .patient(patient)
                .orderDate(orderDate)
                .build();

        examinationRepository.save(examination);

        // when
        boolean foundExamination = examinationRepository.existsByNumber(examination.getNumber());

        // then
        Assert.assertTrue(foundExamination);
    }

    @Test
    @Transactional
    public void givenExamination_whenFindByNumber_thenReturnExamination() throws ParseException {
        // given
        Long number = 1L;
        Material material = Material.builder().name("material name").createdDate(Instant.now()).build();
        Ward ward = Ward.builder().name("ward name").build();
        Patient patient = Patient.builder()
                .firstName("Andrzej")
                .secondName("Strzelba")
                .pesel("90100213231")
                .createdDate(Instant.now())
                .build();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy");
        Date orderDate = simpleDateFormat.parse("1/23/2018 00:00:00");

        material = materialRepository.save(material);
        ward = wardRepository.save(ward);
        patient = patientRepository.save(patient);

        Examination examination = Examination.builder()
                .number(number)
                .ward(ward)
                .material(material)
                .patient(patient)
                .orderDate(orderDate)
                .build();

        examinationRepository.save(examination);

        // when
        Optional<Examination> foundExamination = examinationRepository.findByNumber(examination.getNumber());

        // then
        Assert.assertEquals(Optional.of(examination), foundExamination);
    }

    @Test
    @Transactional
    public void givenExamination_whenFindByPatient_thenReturnExaminations() throws ParseException {
        // given
        Long number1 = 1L;
        Long number2 = 2L;
        Material material1 = Material.builder().name("material name1").createdDate(Instant.now()).build();
        Material material2 = Material.builder().name("material name2").createdDate(Instant.now()).build();
        Ward ward1 = Ward.builder().name("ward name1").build();
        Ward ward2 = Ward.builder().name("ward name2").build();
        Patient patient = Patient.builder()
                .firstName("Andrzej")
                .secondName("Strzelba")
                .pesel("90100213231")
                .createdDate(Instant.now())
                .build();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy");
        Date orderDate = simpleDateFormat.parse("1/23/2018 00:00:00");

        material1 = materialRepository.save(material1);
        material2 = materialRepository.save(material2);
        ward1 = wardRepository.save(ward1);
        ward2 = wardRepository.save(ward2);
        patient = patientRepository.save(patient);

        Examination examination1 = Examination.builder()
                .number(number1)
                .ward(ward1)
                .material(material1)
                .patient(patient)
                .orderDate(orderDate)
                .build();
        examinationRepository.save(examination1);

        Examination examination2 = Examination.builder()
                .number(number2)
                .ward(ward2)
                .material(material2)
                .patient(patient)
                .orderDate(orderDate)
                .build();
        examinationRepository.save(examination2);

        // when
        List<Examination> foundExaminations = examinationRepository.findByPatient(patient);

        // then
        Assert.assertEquals(examination1, foundExaminations.get(0));
        Assert.assertEquals(examination2, foundExaminations.get(1));
    }

    @Test
    @Transactional
    public void givenExamination_whenFindByMaterial_thenReturnExaminations() throws ParseException {
        // given
        Long number1 = 1L;
        Long number2 = 2L;
        Material material = Material.builder().name("material name").createdDate(Instant.now()).build();
        Ward ward1 = Ward.builder().name("ward name1").build();
        Ward ward2 = Ward.builder().name("ward name2").build();
        Patient patient1 = Patient.builder()
                .firstName("Andrzej")
                .secondName("Strzelba")
                .pesel("90100213231")
                .createdDate(Instant.now())
                .build();
        Patient patient2 = Patient.builder()
                .firstName("Jan")
                .secondName("Kowalski")
                .pesel("70100213232")
                .createdDate(Instant.now())
                .build();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy");
        Date orderDate = simpleDateFormat.parse("1/23/2018 00:00:00");

        material = materialRepository.save(material);
        ward1 = wardRepository.save(ward1);
        ward2 = wardRepository.save(ward2);
        patient1 = patientRepository.save(patient1);
        patient2 = patientRepository.save(patient2);

        Examination examination1 = Examination.builder()
                .number(number1)
                .ward(ward1)
                .material(material)
                .patient(patient1)
                .orderDate(orderDate)
                .build();
        examinationRepository.save(examination1);

        Examination examination2 = Examination.builder()
                .number(number2)
                .ward(ward2)
                .material(material)
                .patient(patient2)
                .orderDate(orderDate)
                .build();
        examinationRepository.save(examination2);

        // when
        List<Examination> foundExaminations = examinationRepository.findByMaterial(material);

        // then
        Assert.assertEquals(examination1, foundExaminations.get(0));
        Assert.assertEquals(examination2, foundExaminations.get(1));
    }

    @Test
    @Transactional
    public void givenExamination_whenFindByWard_thenReturnExaminations() throws ParseException {
        // given
        Long number1 = 1L;
        Long number2 = 2L;
        Material material1 = Material.builder().name("material name1").createdDate(Instant.now()).build();
        Material material2 = Material.builder().name("material name2").createdDate(Instant.now()).build();
        Ward ward = Ward.builder().name("ward name").build();
        Patient patient1 = Patient.builder()
                .firstName("Andrzej")
                .secondName("Strzelba")
                .pesel("90100213231")
                .createdDate(Instant.now())
                .build();
        Patient patient2 = Patient.builder()
                .firstName("Jan")
                .secondName("Kowalski")
                .pesel("70100213232")
                .createdDate(Instant.now())
                .build();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy");
        Date orderDate = simpleDateFormat.parse("1/23/2018 00:00:00");

        material1 = materialRepository.save(material1);
        material2 = materialRepository.save(material2);
        ward = wardRepository.save(ward);
        patient1 = patientRepository.save(patient1);
        patient2 = patientRepository.save(patient2);

        Examination examination1 = Examination.builder()
                .number(number1)
                .ward(ward)
                .material(material1)
                .patient(patient1)
                .orderDate(orderDate)
                .build();
        examinationRepository.save(examination1);

        Examination examination2 = Examination.builder()
                .number(number2)
                .ward(ward)
                .material(material2)
                .patient(patient2)
                .orderDate(orderDate)
                .build();
        examinationRepository.save(examination2);

        // when
        List<Examination> foundExaminations = examinationRepository.findByWard(ward);

        // then
        Assert.assertEquals(examination1, foundExaminations.get(0));
        Assert.assertEquals(examination2, foundExaminations.get(1));
    }

    @Test
    @Transactional
    public void givenExamination_whenFindByWardAndMaterial_thenReturnExaminations() throws ParseException {
        // given
        Long number1 = 1L;
        Long number2 = 2L;
        Material material = Material.builder().name("material name").createdDate(Instant.now()).build();
        Ward ward = Ward.builder().name("ward name").build();
        Patient patient1 = Patient.builder()
                .firstName("Andrzej")
                .secondName("Strzelba")
                .pesel("90100213231")
                .createdDate(Instant.now())
                .build();
        Patient patient2 = Patient.builder()
                .firstName("Jan")
                .secondName("Kowalski")
                .pesel("70100213232")
                .createdDate(Instant.now())
                .build();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy");
        Date orderDate1 = simpleDateFormat.parse("1/23/2018 00:00:00");
        Date orderDate2 = simpleDateFormat.parse("2/24/2019 00:00:00");

        material = materialRepository.save(material);
        ward = wardRepository.save(ward);
        patient1 = patientRepository.save(patient1);
        patient2 = patientRepository.save(patient2);

        Examination examination1 = Examination.builder()
                .number(number1)
                .ward(ward)
                .material(material)
                .patient(patient1)
                .orderDate(orderDate1)
                .build();
        examinationRepository.save(examination1);

        Examination examination2 = Examination.builder()
                .number(number2)
                .ward(ward)
                .material(material)
                .patient(patient2)
                .orderDate(orderDate2)
                .build();
        examinationRepository.save(examination2);

        // when
        List<Examination> foundExaminations = examinationRepository.findByWardAndMaterial(ward, material);

        // then
        Assert.assertEquals(examination1, foundExaminations.get(0));
        Assert.assertEquals(examination2, foundExaminations.get(1));
    }
}
