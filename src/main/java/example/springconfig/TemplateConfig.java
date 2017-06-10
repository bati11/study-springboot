package example.springconfig;

import example.view.MyDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TemplateConfig {

    @Bean
    public MyDialect myDialect() {
        return new MyDialect();
    }
}
