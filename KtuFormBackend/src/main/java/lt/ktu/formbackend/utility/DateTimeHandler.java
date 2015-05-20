package lt.ktu.formbackend.utility;

import java.util.Calendar;

/**
 *
 * @author Lukas
 */
public abstract class DateTimeHandler {
    
    public static String getDateTime() {
        Calendar dateHandler = Calendar.getInstance();
        int year = dateHandler.get(Calendar.YEAR);
        int month = dateHandler.get(Calendar.MONTH);
        int day = dateHandler.get(Calendar.DAY_OF_MONTH);
        int hour = dateHandler.get(Calendar.HOUR_OF_DAY);
        int minute = dateHandler.get(Calendar.MINUTE);
        int second = dateHandler.get(Calendar.SECOND);
        String monthString = month < 10 ? 0 + "" + month : "" + month;
        String dayString = day < 10 ? 0 + "" + day : "" + day;
        String hourString = hour < 10 ? 0 + "" + hour : "" + hour;
        String minuteString = minute < 10 ? 0 + "" + minute : "" + minute;
        String secondString = second < 10 ? 0 + "" + second : "" + second;
        return "" + year + "" + monthString + "" + dayString + " " + hourString + ":" + minuteString + ":" + secondString;
    }
    
}
