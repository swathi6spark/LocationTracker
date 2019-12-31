package com.example.savenote;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by Swathi on 9/29/16.
 */

public class Utils {

    public static String getCurrentTimeStamp() {
        String timeStamp = "YYYYMMDDHHMMSS";
        timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Timestamp(System.currentTimeMillis()));
        return timeStamp;
    }
}

