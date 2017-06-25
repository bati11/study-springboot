package example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.ForwardedHeaderFilter;

@SpringBootApplication
public class Application {

    /**
     * for reverse proxy
     */
    @Bean
    public ForwardedHeaderFilter forwardedHeaderFilter() {
        ForwardedHeaderFilter filter = new ForwardedHeaderFilter();
        return filter;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
