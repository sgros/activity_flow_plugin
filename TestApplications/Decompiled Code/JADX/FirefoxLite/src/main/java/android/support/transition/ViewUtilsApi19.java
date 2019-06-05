package android.support.transition;

import android.util.Log;
import android.view.View;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ViewUtilsApi19 extends ViewUtilsBase {
    private static Method sGetTransitionAlphaMethod;
    private static boolean sGetTransitionAlphaMethodFetched;
    private static Method sSetTransitionAlphaMethod;
    private static boolean sSetTransitionAlphaMethodFetched;

    public void clearNonTransitionAlpha(View view) {
    }

    public void saveNonTransitionAlpha(View view) {
    }

    ViewUtilsApi19() {
    }

    public void setTransitionAlpha(View view, float f) {
        fetchSetTransitionAlphaMethod();
        if (sSetTransitionAlphaMethod != null) {
            try {
                sSetTransitionAlphaMethod.invoke(view, new Object[]{Float.valueOf(f)});
                return;
            } catch (IllegalAccessException unused) {
                return;
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e.getCause());
            }
        }
        view.setAlpha(f);
    }

    public float getTransitionAlpha(View view) {
        fetchGetTransitionAlphaMethod();
        if (sGetTransitionAlphaMethod != null) {
            try {
                return ((Float) sGetTransitionAlphaMethod.invoke(view, new Object[0])).floatValue();
            } catch (IllegalAccessException unused) {
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e.getCause());
            }
        }
        return super.getTransitionAlpha(view);
    }

    private void fetchSetTransitionAlphaMethod() {
        if (!sSetTransitionAlphaMethodFetched) {
            try {
                sSetTransitionAlphaMethod = View.class.getDeclaredMethod("setTransitionAlpha", new Class[]{Float.TYPE});
                sSetTransitionAlphaMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                Log.i("ViewUtilsApi19", "Failed to retrieve setTransitionAlpha method", e);
            }
            sSetTransitionAlphaMethodFetched = true;
        }
    }

    private void fetchGetTransitionAlphaMethod() {
        if (!sGetTransitionAlphaMethodFetched) {
            try {
                sGetTransitionAlphaMethod = View.class.getDeclaredMethod("getTransitionAlpha", new Class[0]);
                sGetTransitionAlphaMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                Log.i("ViewUtilsApi19", "Failed to retrieve getTransitionAlpha method", e);
            }
            sGetTransitionAlphaMethodFetched = true;
        }
    }
}
