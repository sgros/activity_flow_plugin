// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.app;

import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Build$VERSION;
import android.app.Activity;
import android.support.v4.content.ContextCompat;

public class ActivityCompat extends ContextCompat
{
    private static PermissionCompatDelegate sDelegate;
    
    public static void finishAffinity(final Activity activity) {
        if (Build$VERSION.SDK_INT >= 16) {
            activity.finishAffinity();
        }
        else {
            activity.finish();
        }
    }
    
    public static PermissionCompatDelegate getPermissionCompatDelegate() {
        return ActivityCompat.sDelegate;
    }
    
    public static void requestPermissions(final Activity activity, final String[] array, final int n) {
        if (ActivityCompat.sDelegate != null && ActivityCompat.sDelegate.requestPermissions(activity, array, n)) {
            return;
        }
        if (Build$VERSION.SDK_INT >= 23) {
            if (activity instanceof RequestPermissionsRequestCodeValidator) {
                ((RequestPermissionsRequestCodeValidator)activity).validateRequestPermissionsRequestCode(n);
            }
            activity.requestPermissions(array, n);
        }
        else if (activity instanceof OnRequestPermissionsResultCallback) {
            new Handler(Looper.getMainLooper()).post((Runnable)new Runnable() {
                @Override
                public void run() {
                    final int[] array = new int[array.length];
                    final PackageManager packageManager = activity.getPackageManager();
                    final String packageName = activity.getPackageName();
                    for (int length = array.length, i = 0; i < length; ++i) {
                        array[i] = packageManager.checkPermission(array[i], packageName);
                    }
                    ((OnRequestPermissionsResultCallback)activity).onRequestPermissionsResult(n, array, array);
                }
            });
        }
    }
    
    public static boolean shouldShowRequestPermissionRationale(final Activity activity, final String s) {
        return Build$VERSION.SDK_INT >= 23 && activity.shouldShowRequestPermissionRationale(s);
    }
    
    public static void startActivityForResult(final Activity activity, final Intent intent, final int n, final Bundle bundle) {
        if (Build$VERSION.SDK_INT >= 16) {
            activity.startActivityForResult(intent, n, bundle);
        }
        else {
            activity.startActivityForResult(intent, n);
        }
    }
    
    public interface OnRequestPermissionsResultCallback
    {
        void onRequestPermissionsResult(final int p0, final String[] p1, final int[] p2);
    }
    
    public interface PermissionCompatDelegate
    {
        boolean onActivityResult(final Activity p0, final int p1, final int p2, final Intent p3);
        
        boolean requestPermissions(final Activity p0, final String[] p1, final int p2);
    }
    
    public interface RequestPermissionsRequestCodeValidator
    {
        void validateRequestPermissionsRequestCode(final int p0);
    }
}
