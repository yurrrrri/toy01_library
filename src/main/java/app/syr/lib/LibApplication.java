package app.syr.lib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LibApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibApplication.class, args);
    }

}
