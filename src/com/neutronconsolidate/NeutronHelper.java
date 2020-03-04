package com.neutronconsolidate;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NeutronHelper {
    public static String stringToRegex(String str) {
        return "\\Q" + str + "\\E";
    }

    public static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(date);
    }
}
