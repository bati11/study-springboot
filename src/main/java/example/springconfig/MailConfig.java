package example.springconfig;

import example.mail.DebugMailSenderImpl;
import example.mail.MyMailSender;
import example.mail.MyMailSenderImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class MailConfig {

    @Bean
    @Profile("!production")
    public MyMailSender debugMailSender(JavaMailSender javaMailSender) {
        return new DebugMailSenderImpl(javaMailSender);
    }

    @Bean
    @Profile("production")
    public MyMailSender mailSender(JavaMailSender javaMailSender) {
        return new MyMailSenderImpl(javaMailSender);
    }
}
