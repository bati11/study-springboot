package example.springconfig;

import example.auth.LoginAccountRepository;
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
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

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

        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedPage("/access-denied");

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

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint("/login?unlogin=true");
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
