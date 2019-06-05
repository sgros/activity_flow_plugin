package android.support.p001v4.view;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewConfiguration;
import java.lang.reflect.Method;

/* renamed from: android.support.v4.view.ViewConfigurationCompat */
public final class ViewConfigurationCompat {
    private static Method sGetScaledScrollFactorMethod;

    static {
        if (VERSION.SDK_INT == 25) {
            try {
                sGetScaledScrollFactorMethod = ViewConfiguration.class.getDeclaredMethod("getScaledScrollFactor", new Class[0]);
            } catch (Exception unused) {
                Log.i("ViewConfigCompat", "Could not find method getScaledScrollFactor() on ViewConfiguration");
            }
        }
    }

    public static float getScaledHorizontalScrollFactor(ViewConfiguration viewConfiguration, Context context) {
        if (VERSION.SDK_INT >= 26) {
            return viewConfiguration.getScaledHorizontalScrollFactor();
        }
        return ViewConfigurationCompat.getLegacyScrollFactor(viewConfiguration, context);
    }

    public static float getScaledVerticalScrollFactor(ViewConfiguration viewConfiguration, Context context) {
        if (VERSION.SDK_INT >= 26) {
            return viewConfiguration.getScaledVerticalScrollFactor();
        }
        return ViewConfigurationCompat.getLegacyScrollFactor(viewConfiguration, context);
    }

    private static float getLegacyScrollFactor(ViewConfiguration viewConfiguration, Context context) {
        if (VERSION.SDK_INT >= 25 && sGetScaledScrollFactorMethod != null) {
            try {
                return (float) ((Integer) sGetScaledScrollFactorMethod.invoke(viewConfiguration, new Object[0])).intValue();
            } catch (Exception unused) {
                Log.i("ViewConfigCompat", "Could not find method getScaledScrollFactor() on ViewConfiguration");
            }
        }
        TypedValue typedValue = new TypedValue();
        return context.getTheme().resolveAttribute(16842829, typedValue, true) ? typedValue.getDimension(context.getResources().getDisplayMetrics()) : 0.0f;
    }

    public static int getScaledHoverSlop(ViewConfiguration viewConfiguration) {
        if (VERSION.SDK_INT >= 28) {
            return viewConfiguration.getScaledHoverSlop();
        }
        return viewConfiguration.getScaledTouchSlop() / 2;
    }

    public static boolean shouldShowMenuShortcutsWhenKeyboardPresent(ViewConfiguration viewConfiguration, Context context) {
        if (VERSION.SDK_INT >= 28) {
            return viewConfiguration.shouldShowMenuShortcutsWhenKeyboardPresent();
        }
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("config_showMenuShortcutsWhenKeyboardPresent", "bool", "android");
        boolean z = identifier != 0 && resources.getBoolean(identifier);
        return z;
    }
}
