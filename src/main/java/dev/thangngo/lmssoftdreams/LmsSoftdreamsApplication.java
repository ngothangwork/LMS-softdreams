package dev.thangngo.lmssoftdreams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LmsSoftdreamsApplication {

    public static void main(String[] args) {
        SpringApplication.run(LmsSoftdreamsApplication.class, args);
    }

}
