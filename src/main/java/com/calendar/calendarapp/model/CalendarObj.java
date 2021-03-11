package com.calendar.calendarapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalendarObj
{
    
    private String title;
    private int startHour;
    private int startMin;
    private int endHour;
    private int endMin;
    private int duration;
    private String startEnd;
    private Date eventDay;
}