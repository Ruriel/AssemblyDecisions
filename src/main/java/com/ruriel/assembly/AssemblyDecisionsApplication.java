package com.ruriel.assembly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class AssemblyDecisionsApplication {
    public static void main(String[] args) {
        SpringApplication.run(AssemblyDecisionsApplication.class, args);
    }
}
