package com.top.antibiotic.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@Profile("!synchronous-tests") // classes using this profile will not use @Async
public class AsyncConfiguration {
}