package com.top.antibiotic.unit.repository;

import com.top.antibiotic.entities.RefreshToken;
import com.top.antibiotic.repository.RefreshTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
public class RefreshTokenRepositoryTests {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Test
    @Transactional
    public void givenRefreshToken_whenFindByRefreshToken_thenReturnToken() {
        // given
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());
        refreshTokenRepository.save(refreshToken);

        // when
        Optional<RefreshToken> foundToken = refreshTokenRepository.findByToken(refreshToken.getToken());

        // then
        Assert.assertEquals(refreshToken, foundToken.get());
    }

    @Test
    @Transactional
    public void givenRefreshToken_whenDeleteByRefreshToken_thenDeleteToken() {
        // given
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());
        refreshTokenRepository.save(refreshToken);

        // when
        refreshTokenRepository.deleteByToken(refreshToken.getToken());

        // then
        Optional<RefreshToken> deletedToken = refreshTokenRepository.findByToken(refreshToken.getToken());
        Assert.assertEquals(Optional.empty(), deletedToken);
    }

}
