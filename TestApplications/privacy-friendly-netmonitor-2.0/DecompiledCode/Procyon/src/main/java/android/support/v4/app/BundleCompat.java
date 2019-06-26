// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.app;

import java.lang.reflect.InvocationTargetException;
import android.util.Log;
import java.lang.reflect.Method;
import android.os.Build$VERSION;
import android.os.IBinder;
import android.os.Bundle;

public final class BundleCompat
{
    private BundleCompat() {
    }
    
    public static IBinder getBinder(final Bundle bundle, final String s) {
        if (Build$VERSION.SDK_INT >= 18) {
            return bundle.getBinder(s);
        }
        return BundleCompatBaseImpl.getBinder(bundle, s);
    }
    
    public static void putBinder(final Bundle bundle, final String s, final IBinder binder) {
        if (Build$VERSION.SDK_INT >= 18) {
            bundle.putBinder(s, binder);
        }
        else {
            BundleCompatBaseImpl.putBinder(bundle, s, binder);
        }
    }
    
    static class BundleCompatBaseImpl
    {
        private static final String TAG = "BundleCompatBaseImpl";
        private static Method sGetIBinderMethod;
        private static boolean sGetIBinderMethodFetched;
        private static Method sPutIBinderMethod;
        private static boolean sPutIBinderMethodFetched;
        
        public static IBinder getBinder(final Bundle obj, final String s) {
            if (!BundleCompatBaseImpl.sGetIBinderMethodFetched) {
                try {
                    (BundleCompatBaseImpl.sGetIBinderMethod = Bundle.class.getMethod("getIBinder", String.class)).setAccessible(true);
                }
                catch (NoSuchMethodException ex) {
                    Log.i("BundleCompatBaseImpl", "Failed to retrieve getIBinder method", (Throwable)ex);
                }
                BundleCompatBaseImpl.sGetIBinderMethodFetched = true;
            }
            if (BundleCompatBaseImpl.sGetIBinderMethod != null) {
                try {
                    return (IBinder)BundleCompatBaseImpl.sGetIBinderMethod.invoke(obj, s);
                }
                catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException ex2) {
                    final Throwable t;
                    Log.i("BundleCompatBaseImpl", "Failed to invoke getIBinder via reflection", t);
                    BundleCompatBaseImpl.sGetIBinderMethod = null;
                }
            }
            return null;
        }
        
        public static void putBinder(final Bundle obj, final String s, final IBinder binder) {
            if (!BundleCompatBaseImpl.sPutIBinderMethodFetched) {
                try {
                    (BundleCompatBaseImpl.sPutIBinderMethod = Bundle.class.getMethod("putIBinder", String.class, IBinder.class)).setAccessible(true);
                }
                catch (NoSuchMethodException ex) {
                    Log.i("BundleCompatBaseImpl", "Failed to retrieve putIBinder method", (Throwable)ex);
                }
                BundleCompatBaseImpl.sPutIBinderMethodFetched = true;
            }
            if (BundleCompatBaseImpl.sPutIBinderMethod != null) {
                try {
                    BundleCompatBaseImpl.sPutIBinderMethod.invoke(obj, s, binder);
                }
                catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException ex2) {
                    final Throwable t;
                    Log.i("BundleCompatBaseImpl", "Failed to invoke putIBinder via reflection", t);
                    BundleCompatBaseImpl.sPutIBinderMethod = null;
                }
            }
        }
    }
}
