package de.ines;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by simon on 29.07.2017.
 */
@SpringBootApplication
@EntityScan(basePackages = { "de.ines.domain" })
@EnableJpaRepositories(basePackages = { "de.ines.repositories" })
public class SmartCityApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(SmartCityApplication.class, args);
    }
}
