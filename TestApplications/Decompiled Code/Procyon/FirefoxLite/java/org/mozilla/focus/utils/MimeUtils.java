// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.utils;

import android.text.TextUtils;
import java.util.regex.Pattern;

public class MimeUtils
{
    private static final Pattern audioPattern;
    private static final Pattern imgPattern;
    private static final Pattern textPattern;
    private static final Pattern videoPattern;
    
    static {
        textPattern = Pattern.compile("^text/[0-9,a-z,A-Z,-,*]+?$");
        imgPattern = Pattern.compile("^image/[0-9,a-z,A-Z,-,*]+?$");
        audioPattern = Pattern.compile("^audio/[0-9,a-z,A-Z,-,*]+?$");
        videoPattern = Pattern.compile("^video/[0-9,a-z,A-Z,-,*]+?$");
    }
    
    public static boolean isImage(final String input) {
        return !TextUtils.isEmpty((CharSequence)input) && MimeUtils.imgPattern.matcher(input).find();
    }
}
