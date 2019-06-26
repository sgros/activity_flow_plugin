// 
// Decompiled by Procyon v0.5.34
// 

package androidx.core.content;

import android.os.Bundle;
import android.content.Intent;
import java.io.File;
import android.graphics.drawable.Drawable;
import android.os.Build$VERSION;
import android.content.res.ColorStateList;
import android.content.Context;
import android.util.TypedValue;

public class ContextCompat
{
    private static final Object sLock;
    private static TypedValue sTempValue;
    
    static {
        sLock = new Object();
    }
    
    public static ColorStateList getColorStateList(final Context context, final int n) {
        if (Build$VERSION.SDK_INT >= 23) {
            return context.getColorStateList(n);
        }
        return context.getResources().getColorStateList(n);
    }
    
    public static Drawable getDrawable(final Context context, int resourceId) {
        final int sdk_INT = Build$VERSION.SDK_INT;
        if (sdk_INT >= 21) {
            return context.getDrawable(resourceId);
        }
        if (sdk_INT >= 16) {
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
    
    public static void startActivity(final Context context, final Intent intent, final Bundle bundle) {
        if (Build$VERSION.SDK_INT >= 16) {
            context.startActivity(intent, bundle);
        }
        else {
            context.startActivity(intent);
        }
    }
}
