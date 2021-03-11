package com.calendar.calendarapp.model;

import com.google.api.client.util.DateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private DateTime eventDay;
}