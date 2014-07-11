package org.smigo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.ArrayList;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationListener authenticationListener;
    @Autowired
    public DataSource dataSource;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //Garden
                .antMatchers("/savegarden/**").permitAll()
                .antMatchers("/garden/**").permitAll()
                .antMatchers("/addyear/**").authenticated()
                .antMatchers("/deleteyear/**").authenticated()
                //Species
                .antMatchers("/addspecies/**").authenticated()
                .antMatchers("/update-species/**").authenticated()
                .antMatchers("/listspecies/**").permitAll()
                .antMatchers("/species/**").permitAll()
                .antMatchers("/visible/**").authenticated()
                .antMatchers("/deletespecies/**").authenticated()
                .antMatchers("/addrule/**").authenticated()
                //User
                .antMatchers("/signup/**").permitAll()
                .antMatchers("/changepassword/**").fullyAuthenticated()
                .antMatchers("/edituser/**").authenticated()
                .antMatchers("/user/**").permitAll()
                .antMatchers("/login/**").permitAll()
                .and()
                .formLogin().loginPage("/login").loginProcessingUrl("/login").successHandler(authenticationListener)
                .and()
                .rememberMe().userDetailsService(customUserDetailsService)
                .key("MjYvVCDYOplXAWq").tokenValiditySeconds(Integer.MAX_VALUE).tokenRepository(persistentTokenRepository())
                .and()
                .userDetailsService(userDetailsService())
                .logout().invalidateHttpSession(true).logoutUrl("/logout").logoutSuccessUrl("/hastalavista")
                .and()
                .csrf().disable()
                .openidLogin().loginPage("/login").loginProcessingUrl("/login-openid").authenticationUserDetailsService(authenticationUserDetailsService()).permitAll();
//                .attributeExchange("https://www.google.com/.*").attribute("axContactEmail").type("http://axschema.org/contact/email").required(true);
    }

    //To pevent default ProviderManager calling getUser twice
    @Override
    protected UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(new ArrayList<UserDetails>());
    }

    @Bean
    public AuthenticationUserDetailsService<OpenIDAuthenticationToken> authenticationUserDetailsService() {
        return new CustomAuthenticationUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
    }

    //Exposing AuthenticationManager in applicationContext
    @Bean(name = "authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

