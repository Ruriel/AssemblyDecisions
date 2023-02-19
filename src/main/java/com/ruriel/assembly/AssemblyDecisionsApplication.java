package com.ruriel.assembly;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.TimeZone;

@SpringBootApplication
@EnableJpaRepositories
public class AssemblyDecisionsApplication {
    public static void main(String[] args) {
        SpringApplication.run(AssemblyDecisionsApplication.class, args);
    }
}
