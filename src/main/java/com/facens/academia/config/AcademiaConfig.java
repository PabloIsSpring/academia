package com.facens.academia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class AcademiaConfig {
    @Bean
    public Clock systemClock() {
        return Clock.systemDefaultZone();
    }

}
