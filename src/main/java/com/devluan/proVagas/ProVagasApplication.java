package com.devluan.proVagas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.devluan.proVagas"})
public class ProVagasApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProVagasApplication.class, args);
    }
}
