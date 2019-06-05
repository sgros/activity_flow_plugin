// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.text;

import java.lang.reflect.InvocationTargetException;
import android.util.Log;
import java.util.Locale;
import java.lang.reflect.Method;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(23)
@RequiresApi(23)
class ICUCompatApi23
{
    private static final String TAG = "ICUCompatIcs";
    private static Method sAddLikelySubtagsMethod;
    
    static {
        try {
            ICUCompatApi23.sAddLikelySubtagsMethod = Class.forName("libcore.icu.ICU").getMethod("addLikelySubtags", Locale.class);
        }
        catch (Exception cause) {
            throw new IllegalStateException(cause);
        }
    }
    
    public static String maximizeAndGetScript(Locale script) {
        try {
            script = ((Locale)ICUCompatApi23.sAddLikelySubtagsMethod.invoke(null, script)).getScript();
            return (String)script;
        }
        catch (InvocationTargetException ex) {
            Log.w("ICUCompatIcs", (Throwable)ex);
        }
        catch (IllegalAccessException ex2) {
            Log.w("ICUCompatIcs", (Throwable)ex2);
            goto Label_0034;
        }
    }
}
