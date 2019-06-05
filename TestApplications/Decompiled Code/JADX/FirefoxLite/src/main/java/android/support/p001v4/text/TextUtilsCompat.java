package android.support.p001v4.text;

import android.os.Build.VERSION;
import android.text.TextUtils;
import java.util.Locale;

/* renamed from: android.support.v4.text.TextUtilsCompat */
public final class TextUtilsCompat {
    private static final Locale ROOT = new Locale("", "");

    public static int getLayoutDirectionFromLocale(Locale locale) {
        if (VERSION.SDK_INT >= 17) {
            return TextUtils.getLayoutDirectionFromLocale(locale);
        }
        if (!(locale == null || locale.equals(ROOT))) {
            String maximizeAndGetScript = ICUCompat.maximizeAndGetScript(locale);
            if (maximizeAndGetScript == null) {
                return TextUtilsCompat.getLayoutDirectionFromFirstChar(locale);
            }
            if (maximizeAndGetScript.equalsIgnoreCase("Arab") || maximizeAndGetScript.equalsIgnoreCase("Hebr")) {
                return 1;
            }
        }
        return 0;
    }

    private static int getLayoutDirectionFromFirstChar(Locale locale) {
        switch (Character.getDirectionality(locale.getDisplayName(locale).charAt(0))) {
            case (byte) 1:
            case (byte) 2:
                return 1;
            default:
                return 0;
        }
    }
}
