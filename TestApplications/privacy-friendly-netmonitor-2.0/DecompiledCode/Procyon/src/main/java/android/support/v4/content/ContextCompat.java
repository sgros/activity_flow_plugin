// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.content;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.util.Log;
import android.os.Build$VERSION;
import android.os.Process;
import android.support.annotation.NonNull;
import android.content.Context;
import java.io.File;
import android.util.TypedValue;

public class ContextCompat
{
    private static final String TAG = "ContextCompat";
    private static final Object sLock;
    private static TypedValue sTempValue;
    
    static {
        sLock = new Object();
    }
    
    protected ContextCompat() {
    }
    
    private static File buildPath(final File file, final String... array) {
        int i = 0;
        final int length = array.length;
        File parent = file;
        while (i < length) {
            final String s = array[i];
            File file2;
            if (parent == null) {
                file2 = new File(s);
            }
            else {
                file2 = parent;
                if (s != null) {
                    file2 = new File(parent, s);
                }
            }
            ++i;
            parent = file2;
        }
        return parent;
    }
    
    public static int checkSelfPermission(@NonNull final Context context, @NonNull final String s) {
        if (s == null) {
            throw new IllegalArgumentException("permission is null");
        }
        return context.checkPermission(s, Process.myPid(), Process.myUid());
    }
    
    public static Context createDeviceProtectedStorageContext(final Context context) {
        if (Build$VERSION.SDK_INT >= 24) {
            return context.createDeviceProtectedStorageContext();
        }
        return null;
    }
    
    private static File createFilesDir(final File file) {
        synchronized (ContextCompat.class) {
            if (file.exists() || file.mkdirs()) {
                return file;
            }
            if (file.exists()) {
                return file;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Unable to create files subdir ");
            sb.append(file.getPath());
            Log.w("ContextCompat", sb.toString());
            return null;
        }
    }
    
    public static File getCodeCacheDir(final Context context) {
        if (Build$VERSION.SDK_INT >= 21) {
            return context.getCodeCacheDir();
        }
        return createFilesDir(new File(context.getApplicationInfo().dataDir, "code_cache"));
    }
    
    @ColorInt
    public static final int getColor(final Context context, @ColorRes final int n) {
        if (Build$VERSION.SDK_INT >= 23) {
            return context.getColor(n);
        }
        return context.getResources().getColor(n);
    }
    
    public static final ColorStateList getColorStateList(final Context context, @ColorRes final int n) {
        if (Build$VERSION.SDK_INT >= 23) {
            return context.getColorStateList(n);
        }
        return context.getResources().getColorStateList(n);
    }
    
    public static File getDataDir(final Context context) {
        if (Build$VERSION.SDK_INT >= 24) {
            return context.getDataDir();
        }
        final String dataDir = context.getApplicationInfo().dataDir;
        File file;
        if (dataDir != null) {
            file = new File(dataDir);
        }
        else {
            file = null;
        }
        return file;
    }
    
    public static final Drawable getDrawable(final Context context, @DrawableRes int resourceId) {
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
    
    public static final File getNoBackupFilesDir(final Context context) {
        if (Build$VERSION.SDK_INT >= 21) {
            return context.getNoBackupFilesDir();
        }
        return createFilesDir(new File(context.getApplicationInfo().dataDir, "no_backup"));
    }
    
    public static File[] getObbDirs(final Context context) {
        if (Build$VERSION.SDK_INT >= 19) {
            return context.getObbDirs();
        }
        return new File[] { context.getObbDir() };
    }
    
    public static boolean isDeviceProtectedStorage(final Context context) {
        return Build$VERSION.SDK_INT >= 24 && context.isDeviceProtectedStorage();
    }
    
    public static boolean startActivities(final Context context, final Intent[] array) {
        return startActivities(context, array, null);
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
    
    public static void startActivity(final Context context, final Intent intent, @Nullable final Bundle bundle) {
        if (Build$VERSION.SDK_INT >= 16) {
            context.startActivity(intent, bundle);
        }
        else {
            context.startActivity(intent);
        }
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