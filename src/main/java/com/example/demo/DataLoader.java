package com.example.demo;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.sql.Date;

@Configuration
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final PatienRepository patienRepository;
    @Override
    public void run(String... args) {

        patienRepository.save(Patient.builder()
                        .id(1L)
                        .ssn("1111111111")
                        .firstname("John")
                        .lastname("Doe")
                        .dob(Date.valueOf("1996-09-10"))
                .build()) ;

    }
}
