package com.calendar.calendarapp.utils;

import com.google.common.base.Strings;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public interface CustomNullChecker
{
    public static <T> boolean nullObjectChecker(T t)
    {
        boolean b;
        if (Objects.isNull(t))
        {
            b = true;
            // throw new NullPointerException("Caught by custom null Object checker");
        }
        else
        {
            b = false;
        }
        return b;
    }
    
    public static boolean emptyNullStringChecker(String s)
    {
        boolean b;
        if (Strings.isNullOrEmpty(s))
        {
            b = true;
        }
        else
        {
            b = false;
        }
        return b;
    }
    
    public static boolean nullStringSChecker(String... s)
    {
        AtomicBoolean b = new AtomicBoolean(false);
        Arrays.stream(s).forEach(d ->
                                 {
                                     if (Strings.isNullOrEmpty(d))
                                     {
                                         b.set(true);
                                     }
                                 });
        return b.get();
    }
    
    public static String stringSafe(String raw)
    {
        return raw != null ? raw : "";
    }
}
