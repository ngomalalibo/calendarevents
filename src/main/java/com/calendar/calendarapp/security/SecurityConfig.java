package com.calendar.calendarapp.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// @EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    
    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity.csrf().disable().antMatcher("/**").authorizeRequests()
                    .antMatchers("/", "/index", "/home").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .oauth2Login().loginPage("/home")
                    .defaultSuccessUrl("https://googleeventsapp.herokuapp.com/welcome", true)
                    .failureUrl("https://googleeventsapp.herokuapp.com/home")
                    .and().
                            logout().logoutSuccessUrl("/");
    }
}
