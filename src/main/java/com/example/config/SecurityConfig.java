package com.example.config;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.example.models.User;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.naming.InitialContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PatrykGrudnik on 14.02.2017.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource datasource;
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("user").password("user").roles("USER")
//                .and()
//                .withUser("admin").password("admin").roles("ADMIN");
////
////        JdbcUserDetailsManager userDetailsService = new JdbcUserDetailsManager();
////        userDetailsService.setDataSource(datasource);
////        PasswordEncoder encoder = new BCryptPasswordEncoder();
////
////        auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
////        auth.jdbcAuthentication().dataSource(datasource);
//    }
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private DataSource dataSource;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.
                jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
             .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.
                authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/").hasAuthority("ADMIN")
                .antMatchers("/cars").hasAuthority("ADMIN")
                .antMatchers("/registration").permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN").anyRequest()
                .authenticated().and().csrf().disable().formLogin()
                //.loginPage("/login").failureUrl("/login?error=true")
                .defaultSuccessUrl("/profile")
                .usernameParameter("mail")
                .passwordParameter("password")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login").and().exceptionHandling();
              //  .accessDeniedPage("/access-denied");
    }
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers("/user").hasRole("ADMIN")
//                .antMatchers("/cars").hasRole("ADMIN")
//                .antMatchers("/index").hasRole("ADMIN")
//                .antMatchers("/panel").hasRole("USER")
//               // .antMatchers("/**").permitAll()
//                .antMatchers("/signup","/").permitAll()
//                .anyRequest().authenticated() // 7
//                .and()
//                .formLogin()
//                .defaultSuccessUrl("/panel/")
//                .and()
//                .logout()
//                .and()
//                .csrf().disable();
//    }
}
