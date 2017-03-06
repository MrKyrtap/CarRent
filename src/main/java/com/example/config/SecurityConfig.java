package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by PatrykGrudnik on 14.02.2017.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user").password("user").roles("USER")
                .and()
                .withUser("admin").password("admin").roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/user").hasRole("ADMIN")
                .antMatchers("/cars").hasRole("ADMIN")
                .antMatchers("/index").hasRole("ADMIN")
                .antMatchers("/panel").hasRole("USER")
               // .antMatchers("/**").permitAll()
                .antMatchers("/signup","/").permitAll() // #4
                .anyRequest().authenticated() // 7
                .and()
                .formLogin()
                .defaultSuccessUrl("/panel/")
                .and()
                .logout()
                .and()
                .csrf().disable();
    }
}