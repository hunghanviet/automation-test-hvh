package utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTimeUtil {
    public static String now(String format) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat fm = new SimpleDateFormat();
        return fm.format(cal.getTime());
    }
}
