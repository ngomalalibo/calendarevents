package com.calendar.calendarapp.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "calendarevents")
public class CalendarEvent
{
    @Id
    private String id;
    private String title;
    private int startHour;
    private int startMin;
    private int endHour;
    private int endMin;
    private int duration;
    private String startEnd;
    private Date eventDay;
}