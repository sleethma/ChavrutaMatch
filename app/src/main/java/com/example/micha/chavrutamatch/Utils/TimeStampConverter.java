package com.example.micha.chavrutamatch.Utils;

import android.content.Context;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by micha on 11/18/2017.
 */

public class TimeStampConverter {

    public static Timestamp convertStringToSqlTimestamp(String timeToConvert) {
        Timestamp timestamp;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(timeToConvert);
            timestamp = new java.sql.Timestamp(parsedDate.getTime());
            return timestamp;
        } catch (Exception e) { //this generic but you can control another types of exception
            // look the origin of excption
            return null;
        }
    }

    public static String convertTimestampToString(Timestamp timeToConvert) {
        Date date = new Date();
        date.setTime(timeToConvert.getTime());
        String dateString = new SimpleDateFormat("MM-dd-yyyy").format(date);
        return dateString;
    }

    public static Date convertStringToTimeStamp(String timeToConvert) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = formatter.parse(timeToConvert);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Date convertStringDate(String timeToConvert) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = formatter.parse(timeToConvert);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }



    public static boolean classIsPassed(String currentDate, String classDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date current = null;
        Date dateOfClass = null;
        try {
            current = dateFormat.parse(currentDate);
            dateOfClass = dateFormat.parse(classDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
            if(dateOfClass.before(current)) {
                return true;
            }else {
                return false;
            }
    }

    public static boolean classDatePassedAndDelete(Context context, String currentDate, String classDate) {
        DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


        Date current = null;
        Date dateOfClass = null;
        try {
            current = dateFormat.parse(currentDate);
            dateOfClass = formatter.parse(classDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(dateOfClass.before(current)) {
            //todo: code to delete from db
            Toast.makeText(context, "delete this class", Toast.LENGTH_SHORT).show();

            return true;
        }else {
            return false;
        }
    }
}
