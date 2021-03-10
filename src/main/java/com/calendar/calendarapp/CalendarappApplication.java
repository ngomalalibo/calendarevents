package com.calendar.calendarapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class CalendarappApplication // extends SpringBootServletInitializer // heroku deployment
{
    /*@Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder)
    {
        return builder.sources(CalendarappApplication.class);
    }*/
    
    public static void main(String[] args)
    {
        SpringApplication.run(CalendarappApplication.class, args);
    }
    
    
}
