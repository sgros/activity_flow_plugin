// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.internal;

import android.support.v7.widget.TintTypedArray;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.content.Context;
import android.support.design.R;

public final class ThemeEnforcement
{
    private static final int[] APPCOMPAT_CHECK_ATTRS;
    private static final int[] MATERIAL_CHECK_ATTRS;
    
    static {
        APPCOMPAT_CHECK_ATTRS = new int[] { R.attr.colorPrimary };
        MATERIAL_CHECK_ATTRS = new int[] { R.attr.colorSecondary };
    }
    
    public static void checkAppCompatTheme(final Context context) {
        checkTheme(context, ThemeEnforcement.APPCOMPAT_CHECK_ATTRS, "Theme.AppCompat");
    }
    
    private static void checkCompatibleTheme(final Context context, final AttributeSet set, final int n, final int n2) {
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.ThemeEnforcement, n, n2);
        final boolean boolean1 = obtainStyledAttributes.getBoolean(R.styleable.ThemeEnforcement_enforceMaterialTheme, false);
        obtainStyledAttributes.recycle();
        if (boolean1) {
            checkMaterialTheme(context);
        }
        checkAppCompatTheme(context);
    }
    
    public static void checkMaterialTheme(final Context context) {
        checkTheme(context, ThemeEnforcement.MATERIAL_CHECK_ATTRS, "Theme.MaterialComponents");
    }
    
    private static void checkTextAppearance(final Context context, final AttributeSet set, final int[] array, final int n, final int n2, final int... array2) {
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.ThemeEnforcement, n, n2);
        if (!obtainStyledAttributes.getBoolean(R.styleable.ThemeEnforcement_enforceTextAppearance, false)) {
            obtainStyledAttributes.recycle();
            return;
        }
        boolean customTextAppearanceValid;
        if (array2 != null && array2.length != 0) {
            customTextAppearanceValid = isCustomTextAppearanceValid(context, set, array, n, n2, array2);
        }
        else {
            customTextAppearanceValid = (obtainStyledAttributes.getResourceId(R.styleable.ThemeEnforcement_android_textAppearance, -1) != -1);
        }
        obtainStyledAttributes.recycle();
        if (customTextAppearanceValid) {
            return;
        }
        throw new IllegalArgumentException("This component requires that you specify a valid TextAppearance attribute. Update your app theme to inherit from Theme.MaterialComponents (or a descendant).");
    }
    
    private static void checkTheme(final Context context, final int[] array, final String str) {
        if (isTheme(context, array)) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("The style on this component requires your app theme to be ");
        sb.append(str);
        sb.append(" (or a descendant).");
        throw new IllegalArgumentException(sb.toString());
    }
    
    private static boolean isCustomTextAppearanceValid(final Context context, final AttributeSet set, final int[] array, int i, int length, final int... array2) {
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, array, i, length);
        for (length = array2.length, i = 0; i < length; ++i) {
            if (obtainStyledAttributes.getResourceId(array2[i], -1) == -1) {
                obtainStyledAttributes.recycle();
                return false;
            }
        }
        obtainStyledAttributes.recycle();
        return true;
    }
    
    private static boolean isTheme(final Context context, final int[] array) {
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(array);
        final boolean hasValue = obtainStyledAttributes.hasValue(0);
        obtainStyledAttributes.recycle();
        return hasValue;
    }
    
    public static TypedArray obtainStyledAttributes(final Context context, final AttributeSet set, final int[] array, final int n, final int n2, final int... array2) {
        checkCompatibleTheme(context, set, n, n2);
        checkTextAppearance(context, set, array, n, n2, array2);
        return context.obtainStyledAttributes(set, array, n, n2);
    }
    
    public static TintTypedArray obtainTintedStyledAttributes(final Context context, final AttributeSet set, final int[] array, final int n, final int n2, final int... array2) {
        checkCompatibleTheme(context, set, n, n2);
        checkTextAppearance(context, set, array, n, n2, array2);
        return TintTypedArray.obtainStyledAttributes(context, set, array, n, n2);
    }
}
