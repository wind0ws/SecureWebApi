package com.threshold.webapiauth.util;

import com.threshold.webapiauth.Configuration;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

/**
 * Created by Threshold on 2015/12/28.
 */
public class Util {

    public static final Charset UTF8 = Charset.forName("UTF-8");

    private static SimpleDateFormat dateTimeFormat;

    public static String getUtcTime() {
        if (dateTimeFormat == null) {
        dateTimeFormat = new SimpleDateFormat(Configuration.XDateFormat, Locale.ENGLISH);
        dateTimeFormat.setTimeZone(new SimpleTimeZone(0, "UTC"));
        }
        return dateTimeFormat.format(new Date());
    }

}
