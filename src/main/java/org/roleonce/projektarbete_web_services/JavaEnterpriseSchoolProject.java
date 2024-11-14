package org.roleonce.projektarbete_web_services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class JavaEnterpriseSchoolProject {

    public static void main(String[] args) {
        SpringApplication.run(JavaEnterpriseSchoolProject.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
