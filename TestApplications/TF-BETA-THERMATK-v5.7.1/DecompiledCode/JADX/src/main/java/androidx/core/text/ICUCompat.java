package androidx.core.text;

import android.icu.util.ULocale;
import android.os.Build.VERSION;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

public final class ICUCompat {
    private static Method sAddLikelySubtagsMethod;
    private static Method sGetScriptMethod;

    static {
        int i = VERSION.SDK_INT;
        String str = "addLikelySubtags";
        String str2 = "libcore.icu.ICU";
        if (i < 21) {
            try {
                Class cls = Class.forName(str2);
                if (cls != null) {
                    sGetScriptMethod = cls.getMethod("getScript", new Class[]{String.class});
                    sAddLikelySubtagsMethod = cls.getMethod(str, new Class[]{String.class});
                }
            } catch (Exception e) {
                sGetScriptMethod = null;
                sAddLikelySubtagsMethod = null;
                Log.w("ICUCompat", e);
            }
        } else if (i < 24) {
            try {
                sAddLikelySubtagsMethod = Class.forName(str2).getMethod(str, new Class[]{Locale.class});
            } catch (Exception e2) {
                throw new IllegalStateException(e2);
            }
        }
    }

    public static String maximizeAndGetScript(Locale locale) {
        String str = "ICUCompat";
        int i = VERSION.SDK_INT;
        if (i >= 24) {
            return ULocale.addLikelySubtags(ULocale.forLocale(locale)).getScript();
        }
        if (i >= 21) {
            try {
                locale = ((Locale) sAddLikelySubtagsMethod.invoke(null, new Object[]{locale})).getScript();
                return locale;
            } catch (InvocationTargetException e) {
                Log.w(str, e);
                return locale.getScript();
            } catch (IllegalAccessException e2) {
                Log.w(str, e2);
                return locale.getScript();
            }
        }
        String addLikelySubtags = addLikelySubtags(locale);
        if (addLikelySubtags != null) {
            return getScript(addLikelySubtags);
        }
        return null;
    }

    private static String getScript(String str) {
        String str2 = "ICUCompat";
        try {
            if (sGetScriptMethod != null) {
                return (String) sGetScriptMethod.invoke(null, new Object[]{str});
            }
        } catch (IllegalAccessException e) {
            Log.w(str2, e);
        } catch (InvocationTargetException e2) {
            Log.w(str2, e2);
        }
        return null;
    }

    private static String addLikelySubtags(Locale locale) {
        String str = "ICUCompat";
        String locale2 = locale.toString();
        try {
            if (sAddLikelySubtagsMethod != null) {
                return (String) sAddLikelySubtagsMethod.invoke(null, new Object[]{locale2});
            }
        } catch (IllegalAccessException e) {
            Log.w(str, e);
        } catch (InvocationTargetException e2) {
            Log.w(str, e2);
        }
        return locale2;
    }
}
