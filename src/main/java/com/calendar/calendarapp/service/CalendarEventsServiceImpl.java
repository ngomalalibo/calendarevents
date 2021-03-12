package com.calendar.calendarapp.service;

import com.calendar.calendarapp.model.CalendarObj;
import com.calendar.calendarapp.repository.CalendarEventsRepository;
import com.calendar.calendarapp.utils.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarEventsServiceImpl implements CalendarEventsService
{
    @Autowired
    CalendarEventsRepository calendarEventsRepository;
    
    @Autowired
    Messages messages;
    
    @Override
    public String saveAll(List<CalendarObj> entity)
    {
        if (calendarEventsRepository.saveAll(entity).size() > 0)
        {
            return messages.getMessages("Calendar saved Successfully", "success");
        }
        else
        {
            return messages.getMessages("Calendar not saved", "error");
        }
    }
    
    @Override
    public List<CalendarObj> findAll()
    {
        return calendarEventsRepository.findAll();
    }
}
