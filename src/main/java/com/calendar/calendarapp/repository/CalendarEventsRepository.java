package com.calendar.calendarapp.repository;

import com.calendar.calendarapp.model.CalendarEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarEventsRepository extends MongoRepository<CalendarEvent, String>
{

}
