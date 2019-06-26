package menion.android.whereyougo.preferences;

import menion.android.whereyougo.MainApplication;

public class Locale {
    public static String getString(int string) {
        if (MainApplication.getContext() != null) {
            return MainApplication.getContext().getString(string);
        }
        return "";
    }

    public static String getString(int string, Object... formatArgs) {
        if (MainApplication.getContext() != null) {
            return MainApplication.getContext().getString(string, formatArgs);
        }
        return "";
    }
}
