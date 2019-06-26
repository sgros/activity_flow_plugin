// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.preferences;

import menion.android.whereyougo.MainApplication;

public class Locale
{
    public static String getString(final int n) {
        String string;
        if (MainApplication.getContext() != null) {
            string = MainApplication.getContext().getString(n);
        }
        else {
            string = "";
        }
        return string;
    }
    
    public static String getString(final int n, final Object... array) {
        String string;
        if (MainApplication.getContext() != null) {
            string = MainApplication.getContext().getString(n, array);
        }
        else {
            string = "";
        }
        return string;
    }
}
