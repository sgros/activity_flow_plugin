package org.mozilla.focus.utils;

public class ProviderUtils {
    public static String getLimitParam(String str, String str2) {
        if (str2 == null) {
            return null;
        }
        if (str == null) {
            return str2;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(",");
        stringBuilder.append(str2);
        return stringBuilder.toString();
    }
}
