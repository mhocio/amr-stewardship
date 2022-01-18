package com.top.antibiotic.unit.repository;

import com.top.antibiotic.entities.User;
import com.top.antibiotic.repository.UserRepository;
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

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
public class UserRepositoryTests {

    @Autowired
    UserRepository userRepository;

    @Test
    public void givenUser_whenFindByUsername_thenReturnUser() {
        // given
        User user = User.builder()
                .username("Andrzej Strzebla")
                .email("andrzejstrzelba@gmail.com")
                .password("P@ssw0rd!")
                .enabled(false)
                .build();
        userRepository.save(user);

        // when
        Optional<User> foundUser = userRepository.findByUsername(user.getUsername());

        // then
        Assert.assertEquals(Optional.of(user), foundUser);
    }

    @Test
    public void givenUser_whenFindByEmail_thenReturnUser() {
        // given
        User user = User.builder()
                .username("Andrzej Strzebla")
                .email("andrzejstrzelba@gmail.com")
                .password("P@ssw0rd!")
                .enabled(false)
                .build();
        userRepository.save(user);

        // when
        Optional<User> foundUser = userRepository.findByEmail(user.getEmail());

        // then
        Assert.assertEquals(Optional.of(user), foundUser);
    }
}
