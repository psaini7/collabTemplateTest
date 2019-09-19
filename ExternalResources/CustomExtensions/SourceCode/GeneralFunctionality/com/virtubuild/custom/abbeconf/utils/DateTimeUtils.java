package com.virtubuild.custom.abbeconf.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * DateTimeUtils - utility functions for getting date and time
 */
public class DateTimeUtils {
    
    /**
     * Print the current date and time to the console in the format
     * "yyyy/MM/dd HH:mm:ss"
     */
    public static void PrintCurrentDateTime() {
        String now = GetCurrentDateTime();
        System.out.println(now);
    }

    /**
     * Gets the current date and time in the format "yyyy/MM/dd HH:mm:ss"
     * @return  A string of the current date and time
     */
    public static String GetCurrentDateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();  
        return dtf.format(now);
    }
    

}
