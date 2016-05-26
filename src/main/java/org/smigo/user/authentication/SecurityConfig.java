package org.smigo.user.authentication;

/*
 * #%L
 * Smigo
 * %%
 * Copyright (C) 2015 Christian Nilsson
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.annotation.web.configurers.openid.OpenIDLoginConfigurer;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationUserDetailsService<OpenIDAuthenticationToken> openIdUserDetailsService;
    @Autowired
    private EmptyAuthenticationSuccessHandler emptyAuthenticationSuccessHandler;
    @Autowired
    public DataSource dataSource;
    @Autowired
    private UserDetailsService customUserDetailsService;
    @Autowired
    private AuthenticationFailureHandler restAuthenticationFailureHandler;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
/*
        HttpSessionSecurityContextRepository repository = new HttpSessionSecurityContextRepository();
        repository.setDisableUrlRewriting(false);
        http.securityContext().securityContextRepository(repository);
*/
        http.authorizeRequests().anyRequest().permitAll();

        FormLoginConfigurer<HttpSecurity> formLogin = http.formLogin();
        formLogin.loginPage("/login");
        formLogin.loginProcessingUrl("/login");
        formLogin.failureHandler(restAuthenticationFailureHandler);
        formLogin.successHandler(emptyAuthenticationSuccessHandler);

        final SpringSocialConfigurer springSocialConfigurer = new SpringSocialConfigurer();
        springSocialConfigurer.postLoginUrl("/garden-planner");
        http.apply(springSocialConfigurer);

        RememberMeConfigurer<HttpSecurity> rememberMe = http.rememberMe();
        rememberMe.userDetailsService(customUserDetailsService);
        rememberMe.tokenValiditySeconds(Integer.MAX_VALUE);
        rememberMe.tokenRepository(persistentTokenRepository());

        LogoutConfigurer<HttpSecurity> logout = http.logout();
        logout.invalidateHttpSession(true);
        logout.logoutUrl("/logout");
        logout.logoutSuccessUrl("/welcome-back");

        CsrfConfigurer<HttpSecurity> csrf = http.csrf();
        csrf.disable();

        //disables CacheControlHeadersWriter
        http.headers().cacheControl().disable();

        OpenIDLoginConfigurer<HttpSecurity> openidLogin = http.openidLogin();
        openidLogin.loginPage("/login");
        openidLogin.loginProcessingUrl("/login-openid");
        openidLogin.authenticationUserDetailsService(openIdUserDetailsService);
        openidLogin.permitAll();
        openidLogin.defaultSuccessUrl("/garden-planner");
//      openidLogin.attributeExchange("https://www.google.com/.*").attribute("axContactEmail").type("http://axschema.org/contact/email").required(true);
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

