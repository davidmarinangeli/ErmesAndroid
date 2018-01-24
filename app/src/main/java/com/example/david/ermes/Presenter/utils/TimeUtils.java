package com.example.david.ermes.Presenter.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by David on 18/07/2017.
 */

public class TimeUtils {

    public static long fromIntToMillis(int year, int month, int day, int hour, int minute) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        cal.set(year, month - 1, day, hour, minute);

        return cal.getTimeInMillis();
    }


    public static Date fromMillisToDate(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        cal.setTimeInMillis(millis);
        // farlo per il resto
        Date date = new Date();
        date.setTime(cal.getTimeInMillis());

        return date;
    }

    public static String fromMillistoYearMonthDay(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        cal.setTimeInMillis(millis);

        return new SimpleDateFormat("dd MMMM yyyy", Locale.ITALY).format(millis);
    }

    public static String fromNumericMonthToString(int month) {

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month);
        date.setTime(cal.getTimeInMillis());
        // trasformo il mese numerico in parola e inserisco la prima lettera in maiuscolo
        String start = new SimpleDateFormat("MMMM", Locale.ITALY).format(date);
        return start.substring(0, 1).toUpperCase() + start.substring(1);
    }

    public static String getFormattedHourMinute(Calendar c) {
        DateFormat df = new SimpleDateFormat("HH:mm", Locale.ITALY);


        return df.format(c.getTime());
    }

    public static int getAgeFromBirth(Long birthDate) {
        if (birthDate != null) {
            Calendar now = Calendar.getInstance();
            Calendar birth = Calendar.getInstance();
            birth.setTimeInMillis(birthDate);

            int year_now = now.get(Calendar.YEAR), year_birth = birth.get(Calendar.YEAR);
            int month_now = now.get(Calendar.MONTH), month_birth = birth.get(Calendar.MONTH);
            int day_now = now.get(Calendar.DAY_OF_MONTH), day_birth = birth.get(Calendar.DAY_OF_MONTH);

            int age = year_now - year_birth;
            if (month_now < month_birth || (month_now == month_birth && day_now < day_birth)) {
                age--;
            }

            return age;
        }

        return 0;
    }

    public static String getFormattedElapsedTime(Long date) {
        if (date != null) {
            Calendar now = Calendar.getInstance();
            Calendar from = Calendar.getInstance();
            from.setTimeInMillis(date);

            int year_now = now.get(Calendar.YEAR), year_from = from.get(Calendar.YEAR);
            int month_now = now.get(Calendar.MONTH), month_from = from.get(Calendar.MONTH);
            int day_now = now.get(Calendar.DAY_OF_MONTH), day_from = from.get(Calendar.DAY_OF_MONTH);
            int hour_now = now.get(Calendar.HOUR_OF_DAY), hour_from = from.get(Calendar.HOUR_OF_DAY);
            int min_now = now.get(Calendar.MINUTE), min_from = from.get(Calendar.MINUTE);
            int sec_now = now.get(Calendar.SECOND), sec_from = from.get(Calendar.SECOND);

            int num_result;
            String time_entity;

            if (year_now > year_from) {
                num_result = year_now - year_from;

                if (num_result == 1) {
                    time_entity = " anno fa";
                } else time_entity = " anni fa";
            } else if (month_now > month_from) {
                num_result = month_now - month_from;

                if (num_result == 1) {
                    time_entity = " mese fa";
                } else time_entity = " mesi fa";
            } else if (day_now > day_from) {
                num_result = day_now - day_from;

                if (num_result == 1) {
                    time_entity = " giorno fa";
                } else time_entity = " giorni fa";
            } else if (hour_now > hour_from) {
                num_result = hour_now - hour_from;

                if (num_result == 1) {
                    time_entity = " ora fa";
                } else time_entity = " ore fa";
            } else if (min_now > min_from) {
                num_result = min_now - min_from;
                time_entity = " min fa";
            } else {
                num_result = sec_now - sec_from;
                time_entity = " sec fa";
            }

            return String.valueOf(num_result) + time_entity;
        }

        return "";
    }
}
