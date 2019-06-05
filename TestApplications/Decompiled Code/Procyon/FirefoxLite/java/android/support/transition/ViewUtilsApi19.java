// 
// Decompiled by Procyon v0.5.34
// 

package android.support.transition;

import java.lang.reflect.InvocationTargetException;
import android.util.Log;
import android.view.View;
import java.lang.reflect.Method;

class ViewUtilsApi19 extends ViewUtilsBase
{
    private static Method sGetTransitionAlphaMethod;
    private static boolean sGetTransitionAlphaMethodFetched;
    private static Method sSetTransitionAlphaMethod;
    private static boolean sSetTransitionAlphaMethodFetched;
    
    private void fetchGetTransitionAlphaMethod() {
        if (!ViewUtilsApi19.sGetTransitionAlphaMethodFetched) {
            try {
                (ViewUtilsApi19.sGetTransitionAlphaMethod = View.class.getDeclaredMethod("getTransitionAlpha", (Class<?>[])new Class[0])).setAccessible(true);
            }
            catch (NoSuchMethodException ex) {
                Log.i("ViewUtilsApi19", "Failed to retrieve getTransitionAlpha method", (Throwable)ex);
            }
            ViewUtilsApi19.sGetTransitionAlphaMethodFetched = true;
        }
    }
    
    private void fetchSetTransitionAlphaMethod() {
        if (!ViewUtilsApi19.sSetTransitionAlphaMethodFetched) {
            try {
                (ViewUtilsApi19.sSetTransitionAlphaMethod = View.class.getDeclaredMethod("setTransitionAlpha", Float.TYPE)).setAccessible(true);
            }
            catch (NoSuchMethodException ex) {
                Log.i("ViewUtilsApi19", "Failed to retrieve setTransitionAlpha method", (Throwable)ex);
            }
            ViewUtilsApi19.sSetTransitionAlphaMethodFetched = true;
        }
    }
    
    @Override
    public void clearNonTransitionAlpha(final View view) {
    }
    
    @Override
    public float getTransitionAlpha(final View obj) {
        this.fetchGetTransitionAlphaMethod();
        if (ViewUtilsApi19.sGetTransitionAlphaMethod == null) {
            goto Label_0043;
        }
        try {
            return (float)ViewUtilsApi19.sGetTransitionAlphaMethod.invoke(obj, new Object[0]);
        }
        catch (InvocationTargetException ex) {
            throw new RuntimeException(ex.getCause());
        }
        catch (IllegalAccessException ex2) {
            goto Label_0043;
        }
    }
    
    @Override
    public void saveNonTransitionAlpha(final View view) {
    }
    
    @Override
    public void setTransitionAlpha(final View obj, final float f) {
        this.fetchSetTransitionAlphaMethod();
        if (ViewUtilsApi19.sSetTransitionAlphaMethod == null) {
            goto Label_0045;
        }
        try {
            ViewUtilsApi19.sSetTransitionAlphaMethod.invoke(obj, f);
            goto Label_0050;
        }
        catch (InvocationTargetException ex) {
            throw new RuntimeException(ex.getCause());
        }
        catch (IllegalAccessException ex2) {
            goto Label_0050;
        }
    }
}
