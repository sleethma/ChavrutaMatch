package com.example.micha.chavrutamatch.Utils;

import android.icu.text.RelativeDateTimeFormatter;

import com.example.micha.chavrutamatch.Data.HostSessionData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by micha on 9/28/2017.
 */

public class ChavrutaUtils {

    public static String createUserFirstLastName(String userFirstName, String userLastName){
        String lastInitial = userLastName.substring(0,1);
        String userFirstLastName = userFirstName + " " + lastInitial + ".";
        return userFirstLastName;
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
