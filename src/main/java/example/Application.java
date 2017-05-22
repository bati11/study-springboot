package example;

import example.view.MyDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

@SpringBootApplication
public class Application {

    @Autowired
    ITemplateResolver templateResolver;

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setEnableSpringELCompiler(true);
        templateEngine.setTemplateResolver(templateResolver);
        templateEngine.addDialect(new MyDialect());
        return templateEngine;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
