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
                    .antMatchers("/", "/home").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .oauth2Login().loginPage("/home")
                    .defaultSuccessUrl("https://googleeventsapp.herokuapp.com/welcome")
                    .failureUrl("https://googleeventsapp.herokuapp.com")
                    .and().
                            logout().logoutSuccessUrl("/");
    }
}
