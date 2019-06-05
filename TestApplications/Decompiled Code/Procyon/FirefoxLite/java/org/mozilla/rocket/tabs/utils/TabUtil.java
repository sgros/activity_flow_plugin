// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.tabs.utils;

import android.text.TextUtils;
import android.os.Bundle;

public final class TabUtil
{
    public static Bundle argument(final String s, final boolean b, final boolean b2) {
        final Bundle bundle = new Bundle();
        if (!TextUtils.isEmpty((CharSequence)s)) {
            bundle.putString("_tab_parent_", s);
        }
        bundle.putBoolean("_tab_external_", b);
        bundle.putBoolean("_tab_focus_", b2);
        return bundle;
    }
    
    public static String getParentId(final Bundle bundle) {
        return bundle.getString("_tab_parent_");
    }
    
    public static boolean isFromExternal(final Bundle bundle) {
        return bundle.getBoolean("_tab_external_", false);
    }
    
    public static boolean toFocus(final Bundle bundle) {
        return bundle.getBoolean("_tab_focus_", false);
    }
}
