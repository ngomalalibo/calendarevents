package com.calendar.calendarapp.utils;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class Messages
{
    public String getMessages(String message, String type)
    {
        StringBuilder builder = new StringBuilder();
        if (type.equalsIgnoreCase("error"))
        {
            return builder.append("<div class=\"alert alert-danger\">")
                          .append(message)
                          .append("</div>").toString();
        }
        else if (type.equalsIgnoreCase("success"))
        {
            return builder.append("<div class=\"alert alert-success\">")
                          .append(message)
                          .append("</div>").toString();
        }
        else if (type.equalsIgnoreCase("info"))
        {
            return builder.append("<div class=\"alert alert-info\">")
                          .append(message)
                          .append("</div>").toString();
        }
        else
        {
            return null;
        }
    }
    
    public String errorMessages(HttpSession session)
    {
        if (session.getAttribute("message") != null)
        {
            String message = "<div class=\"alert alert-danger\">";
            message += session.getAttribute("message");
            message += "</div>";
            session.setAttribute("message", null);
            return message;
        }
        return null;
    }
    
    public String successMessages(HttpSession session)
    {
        if (session.getAttribute("message") != null)
        {
            String message = "<div class=\"alert alert-success\">";
            message += session.getAttribute("message");
            message += "</div>";
            session.setAttribute("message", null);
            return message;
        }
        return null;
    }
}
