package application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "controller")
@ComponentScan(basePackages = "aws")
@ComponentScan(basePackages = "cache")
@ComponentScan(basePackages = "service")
@ComponentScan(basePackages = "webClient")
@Slf4j
public class Application {

    public static void main(String[] args) {
        log.info("Starting application...");
        SpringApplication.run(Application.class, args);
    }

}
