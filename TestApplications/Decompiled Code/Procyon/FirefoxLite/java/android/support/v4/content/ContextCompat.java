// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.content;

import android.os.Bundle;
import android.content.Intent;
import java.io.File;
import android.graphics.drawable.Drawable;
import android.content.res.ColorStateList;
import android.os.Build$VERSION;
import android.os.Process;
import android.content.Context;
import android.util.TypedValue;

public class ContextCompat
{
    private static final Object sLock;
    private static TypedValue sTempValue;
    
    static {
        sLock = new Object();
    }
    
    public static int checkSelfPermission(final Context context, final String s) {
        if (s != null) {
            return context.checkPermission(s, Process.myPid(), Process.myUid());
        }
        throw new IllegalArgumentException("permission is null");
    }
    
    public static int getColor(final Context context, final int n) {
        if (Build$VERSION.SDK_INT >= 23) {
            return context.getColor(n);
        }
        return context.getResources().getColor(n);
    }
    
    public static ColorStateList getColorStateList(final Context context, final int n) {
        if (Build$VERSION.SDK_INT >= 23) {
            return context.getColorStateList(n);
        }
        return context.getResources().getColorStateList(n);
    }
    
    public static Drawable getDrawable(final Context context, int resourceId) {
        if (Build$VERSION.SDK_INT >= 21) {
            return context.getDrawable(resourceId);
        }
        if (Build$VERSION.SDK_INT >= 16) {
            return context.getResources().getDrawable(resourceId);
        }
        synchronized (ContextCompat.sLock) {
            if (ContextCompat.sTempValue == null) {
                ContextCompat.sTempValue = new TypedValue();
            }
            context.getResources().getValue(resourceId, ContextCompat.sTempValue, true);
            resourceId = ContextCompat.sTempValue.resourceId;
            // monitorexit(ContextCompat.sLock)
            return context.getResources().getDrawable(resourceId);
        }
    }
    
    public static File[] getExternalCacheDirs(final Context context) {
        if (Build$VERSION.SDK_INT >= 19) {
            return context.getExternalCacheDirs();
        }
        return new File[] { context.getExternalCacheDir() };
    }
    
    public static File[] getExternalFilesDirs(final Context context, final String s) {
        if (Build$VERSION.SDK_INT >= 19) {
            return context.getExternalFilesDirs(s);
        }
        return new File[] { context.getExternalFilesDir(s) };
    }
    
    public static boolean startActivities(final Context context, final Intent[] array, final Bundle bundle) {
        if (Build$VERSION.SDK_INT >= 16) {
            context.startActivities(array, bundle);
        }
        else {
            context.startActivities(array);
        }
        return true;
    }
    
    public static void startForegroundService(final Context context, final Intent intent) {
        if (Build$VERSION.SDK_INT >= 26) {
            context.startForegroundService(intent);
        }
        else {
            context.startService(intent);
        }
    }
}
