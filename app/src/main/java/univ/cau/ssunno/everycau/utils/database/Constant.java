package univ.cau.ssunno.everycau.utils.database;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.SynchronousQueue;

public class Constant {
    // campus code
    public static final int CAMPUS_SEOUL = 0;
    public static final int CAMPUS_ANSUNG = 1;
    // cafeteria code
    public static final int CAFE_SEULKI = 2;
    public static final int CAFE_CHARM = 3;
    public static final int CAFE_STUDENT = 6;
    public static final int CAFE_EMPOLYEE = 7;
    public static final int CAFE_DORMITORY = 8;
    public static final int CAFE_UNIVERSITY = 11;
    public static final int CAFE_NEW_DORMITORY = 12;
    // quarter code
    public static final int QUARTER_ALLDAY = 20;
    public static final int QUARTER_BREAKFAST = 21;
    public static final int QUARTER_LUNCH = 22;
    public static final int QUARTER_DINNER = 23;

    public static String getCurrentDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    public static int getCurrentTime() {
        Date time = new Date(System.currentTimeMillis());
        SimpleDateFormat hour = new SimpleDateFormat("HH", java.util.Locale.getDefault());
        SimpleDateFormat min = new SimpleDateFormat("mm", java.util.Locale.getDefault());
        return Integer.parseInt(hour.format(time)) * 60 + Integer.parseInt(min.format(time));
    }

}
