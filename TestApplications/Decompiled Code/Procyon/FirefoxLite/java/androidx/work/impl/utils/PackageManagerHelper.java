// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.utils;

import android.content.pm.PackageManager;
import android.content.ComponentName;
import android.content.Context;
import androidx.work.Logger;

public class PackageManagerHelper
{
    private static final String TAG;
    
    static {
        TAG = Logger.tagWithPrefix("PackageManagerHelper");
    }
    
    public static void setComponentEnabled(final Context context, final Class clazz, final boolean b) {
        try {
            final PackageManager packageManager = context.getPackageManager();
            final ComponentName componentName = new ComponentName(context, clazz.getName());
            int n;
            if (b) {
                n = 1;
            }
            else {
                n = 2;
            }
            packageManager.setComponentEnabledSetting(componentName, n, 1);
            final Logger value = Logger.get();
            final String tag = PackageManagerHelper.TAG;
            final String name = clazz.getName();
            String s;
            if (b) {
                s = "enabled";
            }
            else {
                s = "disabled";
            }
            value.debug(tag, String.format("%s %s", name, s), new Throwable[0]);
        }
        catch (Exception ex) {
            final Logger value2 = Logger.get();
            final String tag2 = PackageManagerHelper.TAG;
            final String name2 = clazz.getName();
            String s2;
            if (b) {
                s2 = "enabled";
            }
            else {
                s2 = "disabled";
            }
            value2.debug(tag2, String.format("%s could not be %s", name2, s2), ex);
        }
    }
}
