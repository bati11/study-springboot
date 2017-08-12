package example.springconfig;

import example.auth.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MyAccessDeniedHandler myAccessDeniedHandler;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/users").authenticated()
                .antMatchers(HttpMethod.GET, "/users/{userId}").permitAll()
                .antMatchers("/users/{userId}/destroy").access("isAuthenticated() and hasAuthority('ADMIN')")
                .antMatchers("/users/{userId}/**").access("@webSecurity.checkUserId(authentication,#userId)")
                .anyRequest().permitAll();

        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(myAccessDeniedHandler);

        http.rememberMe()
                .tokenRepository(createTokenRepository())
                .useSecureCookie(rememberMeCookieSecureOnly);

        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/login/success", true)
                .failureHandler(failureHandler())
                .and();

        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout**"))
                .logoutSuccessUrl("/login")
                .permitAll();
    }

    @Autowired
    ForwardingUrl forwardingUrl;

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
        return new MyLoginUrlAuthenticationEntryPoint(forwardingUrl, "/login?unlogin=true");
    }

    @Bean
    example.auth.WebSecurity webSecurity() {
        return new example.auth.WebSecurity();
    }

    @Bean
    AuthenticationFailureHandler failureHandler() {
        Map<String, String> exceptionMappings = new HashMap<>();
        exceptionMappings.put(NotActivationException.class.getName(), "/login?unactivated=true");
        ExceptionMappingAuthenticationFailureHandler failureHandler = new ExceptionMappingAuthenticationFailureHandler();

        failureHandler.setDefaultFailureUrl("/login?error=true");
        failureHandler.setExceptionMappings(exceptionMappings);
        return failureHandler;
    }

    /*
     * Settings for custom UserDetailsService
     */

    @Autowired
    LoginAccountRepository loginAccountRepository;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureAuthenticationManager(AuthenticationManagerBuilder auth) throws Exception {
        MyAuthenticationProvider myAuthenticationProvider = new MyAuthenticationProvider();
        myAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        myAuthenticationProvider.setUserDetailsService(loginAccountRepository);
        auth.authenticationProvider(myAuthenticationProvider);
    }


    /*
     * Settings for Remember Me
     */

    @Value("${rememberMeCookieSecureOnly:true}")
    boolean rememberMeCookieSecureOnly;

    @Autowired
    public DataSource dataSource;

    public PersistentTokenRepository createTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }
}
