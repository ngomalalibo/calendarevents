package com.calendar.calendarapp.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity.csrf().disable().antMatcher("/**").authorizeRequests()
                    .antMatchers("/", "/login", "/logout").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .oauth2Login()
                    .defaultSuccessUrl("https://googleeventsapp.herokuapp.com/welcome")
                    .and().logout().logoutSuccessUrl("https://googleeventsapp.herokuapp.com");
        
        httpSecurity.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
    }
}
