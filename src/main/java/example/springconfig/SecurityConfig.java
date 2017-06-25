package example.springconfig;

import example.auth.ForwardingUrl;
import example.auth.LoginAccountRepository;
import example.auth.MyAccessDeniedHandler;
import example.auth.MyLoginUrlAuthenticationEntryPoint;
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
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

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
                .failureUrl("/login?error=true")
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
        auth.userDetailsService(loginAccountRepository).passwordEncoder(passwordEncoder());
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
