package android.support.design.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.C0028R;
import android.support.p004v7.widget.TintTypedArray;
import android.util.AttributeSet;

public final class ThemeEnforcement {
    private static final int[] APPCOMPAT_CHECK_ATTRS = new int[]{C0028R.attr.colorPrimary};
    private static final int[] MATERIAL_CHECK_ATTRS = new int[]{C0028R.attr.colorSecondary};

    public static TypedArray obtainStyledAttributes(Context context, AttributeSet attributeSet, int[] iArr, int i, int i2, int... iArr2) {
        checkCompatibleTheme(context, attributeSet, i, i2);
        checkTextAppearance(context, attributeSet, iArr, i, i2, iArr2);
        return context.obtainStyledAttributes(attributeSet, iArr, i, i2);
    }

    public static TintTypedArray obtainTintedStyledAttributes(Context context, AttributeSet attributeSet, int[] iArr, int i, int i2, int... iArr2) {
        checkCompatibleTheme(context, attributeSet, i, i2);
        checkTextAppearance(context, attributeSet, iArr, i, i2, iArr2);
        return TintTypedArray.obtainStyledAttributes(context, attributeSet, iArr, i, i2);
    }

    private static void checkCompatibleTheme(Context context, AttributeSet attributeSet, int i, int i2) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0028R.styleable.ThemeEnforcement, i, i2);
        boolean z = obtainStyledAttributes.getBoolean(C0028R.styleable.ThemeEnforcement_enforceMaterialTheme, false);
        obtainStyledAttributes.recycle();
        if (z) {
            checkMaterialTheme(context);
        }
        checkAppCompatTheme(context);
    }

    private static void checkTextAppearance(Context context, AttributeSet attributeSet, int[] iArr, int i, int i2, int... iArr2) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0028R.styleable.ThemeEnforcement, i, i2);
        if (obtainStyledAttributes.getBoolean(C0028R.styleable.ThemeEnforcement_enforceTextAppearance, false)) {
            boolean isCustomTextAppearanceValid = (iArr2 == null || iArr2.length == 0) ? obtainStyledAttributes.getResourceId(C0028R.styleable.ThemeEnforcement_android_textAppearance, -1) != -1 : isCustomTextAppearanceValid(context, attributeSet, iArr, i, i2, iArr2);
            obtainStyledAttributes.recycle();
            if (!isCustomTextAppearanceValid) {
                throw new IllegalArgumentException("This component requires that you specify a valid TextAppearance attribute. Update your app theme to inherit from Theme.MaterialComponents (or a descendant).");
            }
            return;
        }
        obtainStyledAttributes.recycle();
    }

    private static boolean isCustomTextAppearanceValid(Context context, AttributeSet attributeSet, int[] iArr, int i, int i2, int... iArr2) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr, i, i2);
        for (int i22 : iArr2) {
            if (obtainStyledAttributes.getResourceId(i22, -1) == -1) {
                obtainStyledAttributes.recycle();
                return false;
            }
        }
        obtainStyledAttributes.recycle();
        return true;
    }

    public static void checkAppCompatTheme(Context context) {
        checkTheme(context, APPCOMPAT_CHECK_ATTRS, "Theme.AppCompat");
    }

    public static void checkMaterialTheme(Context context) {
        checkTheme(context, MATERIAL_CHECK_ATTRS, "Theme.MaterialComponents");
    }

    private static boolean isTheme(Context context, int[] iArr) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(iArr);
        boolean hasValue = obtainStyledAttributes.hasValue(0);
        obtainStyledAttributes.recycle();
        return hasValue;
    }

    private static void checkTheme(Context context, int[] iArr, String str) {
        if (!isTheme(context, iArr)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The style on this component requires your app theme to be ");
            stringBuilder.append(str);
            stringBuilder.append(" (or a descendant).");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }
}
