// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android.utils;

import locus.api.utils.Logger;
import android.database.Cursor;

public class Utils extends locus.api.utils.Utils
{
    private static final String TAG;
    
    static {
        TAG = Utils.class.getSimpleName();
    }
    
    public static void closeQuietly(final Cursor obj) {
        if (obj == null) {
            return;
        }
        try {
            obj.close();
        }
        catch (Exception obj2) {
            Logger.logE(Utils.TAG, "closeQuietly(" + obj + "), e:" + obj2);
            obj2.printStackTrace();
        }
    }
}
