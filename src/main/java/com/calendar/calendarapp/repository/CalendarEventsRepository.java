package com.calendar.calendarapp.repository;

import com.calendar.calendarapp.model.CalendarObj;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarEventsRepository extends MongoRepository<CalendarObj, String>
{

}
