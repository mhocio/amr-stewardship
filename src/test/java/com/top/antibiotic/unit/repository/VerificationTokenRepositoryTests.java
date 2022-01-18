package com.top.antibiotic.unit.repository;

import com.top.antibiotic.entities.VerificationToken;
import com.top.antibiotic.repository.VerificationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
public class VerificationTokenRepositoryTests {

    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    @Test
    public void givenVerificationToken_whenFindByToken_thenReturnToken() {
        // given
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationTokenRepository.save(verificationToken);

        // when
        Optional<VerificationToken> foundToken = verificationTokenRepository.findByToken(token);

        // then
        foundToken.ifPresent(value -> Assert.assertEquals(value.getToken(), token));
    }
}
