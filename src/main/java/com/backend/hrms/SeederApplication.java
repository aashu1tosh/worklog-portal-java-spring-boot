package com.backend.hrms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SeederApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeederApplication.class, args)
                         .getBean(DataSeeder.class)
                         .seed();
    }
}
