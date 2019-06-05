package org.mozilla.rocket.tabs.utils;

import android.os.Bundle;
import android.text.TextUtils;

public final class TabUtil {
    public static Bundle argument(String str, boolean z, boolean z2) {
        Bundle bundle = new Bundle();
        if (!TextUtils.isEmpty(str)) {
            bundle.putString("_tab_parent_", str);
        }
        bundle.putBoolean("_tab_external_", z);
        bundle.putBoolean("_tab_focus_", z2);
        return bundle;
    }

    public static String getParentId(Bundle bundle) {
        return bundle.getString("_tab_parent_");
    }

    public static boolean isFromExternal(Bundle bundle) {
        return bundle.getBoolean("_tab_external_", false);
    }

    public static boolean toFocus(Bundle bundle) {
        return bundle.getBoolean("_tab_focus_", false);
    }
}
