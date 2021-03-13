package com.calendar.calendarapp.service;

import com.calendar.calendarapp.model.CalendarEvent;

import java.util.List;

public interface CalendarEventsService
{
    // List<CalendarObj> findAll();
    //
    // CalendarObj findById(String id);
    //
    String saveAll(List<CalendarEvent> entities);
    
    List<CalendarEvent> findAll();
    //
    // CalendarObj update(CalendarObj entity);
    //
    // void deleteById(String var1);
    //
    // boolean existsById(String var1);
    //
    // long count();
}
