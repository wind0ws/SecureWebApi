package com.threshold.webapiauth.util;

/**
 * As Name you can see：Utils of Text。
 * Created by Threshold on 2016/1/5.
 */
public class TextUtils {
    /**
     * Returns true if the string is null or 0-length.
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }
}
