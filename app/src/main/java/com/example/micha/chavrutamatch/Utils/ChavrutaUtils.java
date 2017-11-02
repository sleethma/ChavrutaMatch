package com.example.micha.chavrutamatch.Utils;

import android.content.Context;
import android.icu.text.RelativeDateTimeFormatter;
import android.support.v7.app.AppCompatActivity;

import com.example.micha.chavrutamatch.Data.HostSessionData;
import com.example.micha.chavrutamatch.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by micha on 9/28/2017.
 */

public class ChavrutaUtils {

    public static String createUserFirstLastName(String userFirstName, String userLastName) {
        String lastInitial = userLastName.substring(0, 1);
        String userFirstLastName = userFirstName + " " + lastInitial + ".";
        return userFirstLastName;
    }

    //gets JsonFile and returns string of data
    public String getJsonFileFromResource(Context context) {
        String json = null;
        try {
            InputStream is = context.getResources().openRawResource(R.raw.us_cities);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


        //parses city and state from json data
        public List<String> parseCityName (String jsonString){
            List<String> top1000CitiesStatesUS = new ArrayList<>();
            String jsonStringCityStateNames = jsonString;

            try {

                JSONObject jsonObject1000CityStates = new JSONObject(jsonStringCityStateNames);
                JSONArray jsonArray = jsonObject1000CityStates.getJSONArray("us_city_names");

                int count = 0;
                String city;
                String state;
                while (count < jsonArray.length()) {
                    JSONObject jo = jsonArray.getJSONObject(count);
                    city = jo.getString("city");
                    state = jo.getString("state");

                    //add city and state to list
                    top1000CitiesStatesUS.add(city + "," + state);
                    String content_added = top1000CitiesStatesUS.get(count);
                    count++;
                }
                top1000CitiesStatesUS.size();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return top1000CitiesStatesUS;
        }

    }
//    static ArrayList<HostSessionData> mUnsortedChavrutaArray;
//
////todo: sort date for mychavruta array sequence by date, must save in date format
//    public static ArrayList<HostSessionData> sortArrayByDate(ArrayList<HostSessionData> unsortedChavrutaArray) {
//        ArrayList<HostSessionData> sortedArrayByScheduledDate = new ArrayList<>();
//        int arraySize = unsortedChavrutaArray.size();
//        //loops through array pulling min date of chavrutaStart and adding to @return
//        for (int i = 1; i <= arraySize; i++) {
//
//            //@min is largest string value possible of all times
//            int min = 10000000;
//            int test0 = "9-5-2017".compareTo("9-15-2017");
//            int tests1 = "9-5-2017".compareTo("10-5-2017");
//            int tests2 = "9-5-2017".compareTo("10-1-2017");
//            int tests3 = "1-31-2017".compareTo("2-1-2017");
//            RelativeDateTimeFormatter date = RelativeDateTimeFormatter.parse("04/02/2011 20:27:05",
//                    DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss"));
//
//
//
//            HostSessionData addMinValueItemToReturnArray = unsortedChavrutaArray.get(0);
//            for (HostSessionData hsd : unsortedChavrutaArray) {
//
//                int newMinStringValue = String.valueOf(hsd.getmSessionDate()).compareTo(String.valueOf(min));
//                //compareTo reterns 8 if argument bigger, 1 if smaller
//                if (newMinStringValue < 7) {
//                    min = newMinStringValue;
//                    addMinValueItemToReturnArray = hsd;
////                    unsortedChavrutaArray.remove(hsd);
////                    arraySize -= 1;
//                }
//            }
//            sortedArrayByScheduledDate.add(addMinValueItemToReturnArray);
//
//        }
//        return mUnsortedChavrutaArray;
//
//    }
//
//
//}
//
//
//
