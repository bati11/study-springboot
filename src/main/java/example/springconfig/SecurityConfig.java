package example.springconfig;

import example.auth.LoginAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/").permitAll()
                .antMatchers(HttpMethod.GET, "/help").permitAll()
                .antMatchers(HttpMethod.GET, "/about").permitAll()
                .antMatchers(HttpMethod.GET, "/contact").permitAll()
                .antMatchers(HttpMethod.GET, "/login").permitAll()
                .antMatchers(HttpMethod.GET, "/signup").permitAll()
                .antMatchers(HttpMethod.GET, "/css/**").permitAll()
                .antMatchers(HttpMethod.GET, "/images/**").permitAll()
                .antMatchers(HttpMethod.GET, "/favicon.ico").permitAll()
                .antMatchers(HttpMethod.POST, "/users").permitAll()
                .antMatchers("/access-denied").permitAll()
                .anyRequest().authenticated();
        http.exceptionHandling().accessDeniedPage("/access-denied");

        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/login/success", true)
                .failureUrl("/login?error=true")
                .and();

        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout**"))
                .logoutSuccessUrl("/login")
                .permitAll();
    }

    @Autowired
    LoginAccountRepository loginAccountRepository;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureAuthenticationManager(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginAccountRepository).passwordEncoder(passwordEncoder());
    }
}
