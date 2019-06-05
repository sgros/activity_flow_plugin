// 
// Decompiled by Procyon v0.5.34
// 

package android.support.transition;

import java.lang.reflect.InvocationTargetException;
import android.util.Log;
import android.view.ViewGroup;
import java.lang.reflect.Method;

class ViewGroupUtilsApi18
{
    private static Method sSuppressLayoutMethod;
    private static boolean sSuppressLayoutMethodFetched;
    
    private static void fetchSuppressLayoutMethod() {
        if (!ViewGroupUtilsApi18.sSuppressLayoutMethodFetched) {
            try {
                (ViewGroupUtilsApi18.sSuppressLayoutMethod = ViewGroup.class.getDeclaredMethod("suppressLayout", Boolean.TYPE)).setAccessible(true);
            }
            catch (NoSuchMethodException ex) {
                Log.i("ViewUtilsApi18", "Failed to retrieve suppressLayout method", (Throwable)ex);
            }
            ViewGroupUtilsApi18.sSuppressLayoutMethodFetched = true;
        }
    }
    
    static void suppressLayout(final ViewGroup obj, final boolean b) {
        fetchSuppressLayoutMethod();
        if (ViewGroupUtilsApi18.sSuppressLayoutMethod != null) {
            try {
                ViewGroupUtilsApi18.sSuppressLayoutMethod.invoke(obj, b);
            }
            catch (InvocationTargetException ex) {
                Log.i("ViewUtilsApi18", "Error invoking suppressLayout method", (Throwable)ex);
            }
            catch (IllegalAccessException ex2) {
                Log.i("ViewUtilsApi18", "Failed to invoke suppressLayout method", (Throwable)ex2);
            }
        }
    }
}
