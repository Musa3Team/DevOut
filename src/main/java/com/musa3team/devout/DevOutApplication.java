package com.musa3team.devout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DevOutApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevOutApplication.class, args);
    }

}
