package com.calendar.calendarapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class CalendarController
{
    
    @GetMapping({"/home", "", "/"})
    public String getHome(Model model)
    {
        
        return "home";
    }
    
    @GetMapping("/welcome")
    public String welcome(Model model)
    {
        return "welcome";
    }
    
    @GetMapping("/index")
    public String index(Model model)
    {
        return "index";
    }
}
