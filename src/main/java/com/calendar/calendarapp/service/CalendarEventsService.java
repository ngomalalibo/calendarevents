package com.calendar.calendarapp.service;

import com.calendar.calendarapp.model.CalendarObj;

import java.util.List;

public interface CalendarEventsService
{
    // List<CalendarObj> findAll();
    //
    // CalendarObj findById(String id);
    //
    String saveAll(List<CalendarObj> entities);
    
    List<CalendarObj> findAll();
    //
    // CalendarObj update(CalendarObj entity);
    //
    // void deleteById(String var1);
    //
    // boolean existsById(String var1);
    //
    // long count();
}
