package org.mozilla.focus.utils;

import android.text.TextUtils;
import java.util.regex.Pattern;

public class MimeUtils {
    private static final Pattern audioPattern = Pattern.compile("^audio/[0-9,a-z,A-Z,-,*]+?$");
    private static final Pattern imgPattern = Pattern.compile("^image/[0-9,a-z,A-Z,-,*]+?$");
    private static final Pattern textPattern = Pattern.compile("^text/[0-9,a-z,A-Z,-,*]+?$");
    private static final Pattern videoPattern = Pattern.compile("^video/[0-9,a-z,A-Z,-,*]+?$");

    public static boolean isImage(String str) {
        return !TextUtils.isEmpty(str) && imgPattern.matcher(str).find();
    }
}
