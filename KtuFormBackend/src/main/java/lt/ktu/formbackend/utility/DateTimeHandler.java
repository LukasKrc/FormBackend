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
        // 2015-08-03 15:05:03 - year[4] + month[2] + day[2] + " " + hour[2] + ":" + min[2] + ":" + sec[2]
        return String.format("%04d%02d%02d %02d:%02d:%02d", year, month, day, hour, minute, second);
    }
    
}
