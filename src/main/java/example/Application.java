package example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class Application {
    @Value("${spring.thymeleaf.cache:}")
    private String springThymeleafCache;

    @RequestMapping("/hello")
    String home() {
        return springThymeleafCache;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
