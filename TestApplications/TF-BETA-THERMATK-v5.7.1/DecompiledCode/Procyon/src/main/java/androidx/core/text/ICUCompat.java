// 
// Decompiled by Procyon v0.5.34
// 

package androidx.core.text;

import android.icu.util.ULocale;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import android.util.Log;
import android.os.Build$VERSION;
import java.lang.reflect.Method;

public final class ICUCompat
{
    private static Method sAddLikelySubtagsMethod;
    private static Method sGetScriptMethod;
    
    static {
        final int sdk_INT = Build$VERSION.SDK_INT;
        if (sdk_INT < 21) {
            try {
                final Class<?> forName = Class.forName("libcore.icu.ICU");
                if (forName != null) {
                    ICUCompat.sGetScriptMethod = forName.getMethod("getScript", String.class);
                    ICUCompat.sAddLikelySubtagsMethod = forName.getMethod("addLikelySubtags", String.class);
                }
            }
            catch (Exception ex) {
                ICUCompat.sGetScriptMethod = null;
                ICUCompat.sAddLikelySubtagsMethod = null;
                Log.w("ICUCompat", (Throwable)ex);
            }
        }
        else if (sdk_INT < 24) {
            try {
                ICUCompat.sAddLikelySubtagsMethod = Class.forName("libcore.icu.ICU").getMethod("addLikelySubtags", Locale.class);
            }
            catch (Exception cause) {
                throw new IllegalStateException(cause);
            }
        }
    }
    
    private static String addLikelySubtags(Locale string) {
        string = (Locale)string.toString();
        try {
            if (ICUCompat.sAddLikelySubtagsMethod != null) {
                return (String)ICUCompat.sAddLikelySubtagsMethod.invoke(null, string);
            }
        }
        catch (InvocationTargetException ex) {
            Log.w("ICUCompat", (Throwable)ex);
        }
        catch (IllegalAccessException ex2) {
            Log.w("ICUCompat", (Throwable)ex2);
        }
        return (String)string;
    }
    
    private static String getScript(String s) {
        try {
            if (ICUCompat.sGetScriptMethod != null) {
                s = (String)ICUCompat.sGetScriptMethod.invoke(null, s);
                return s;
            }
        }
        catch (InvocationTargetException ex) {
            Log.w("ICUCompat", (Throwable)ex);
        }
        catch (IllegalAccessException ex2) {
            Log.w("ICUCompat", (Throwable)ex2);
        }
        return null;
    }
    
    public static String maximizeAndGetScript(final Locale locale) {
        final int sdk_INT = Build$VERSION.SDK_INT;
        if (sdk_INT >= 24) {
            return ULocale.addLikelySubtags(ULocale.forLocale(locale)).getScript();
        }
        if (sdk_INT >= 21) {
            try {
                return ((Locale)ICUCompat.sAddLikelySubtagsMethod.invoke(null, locale)).getScript();
            }
            catch (IllegalAccessException ex) {
                Log.w("ICUCompat", (Throwable)ex);
            }
            catch (InvocationTargetException ex2) {
                Log.w("ICUCompat", (Throwable)ex2);
            }
            return locale.getScript();
        }
        final String addLikelySubtags = addLikelySubtags(locale);
        if (addLikelySubtags != null) {
            return getScript(addLikelySubtags);
        }
        return null;
    }
}
