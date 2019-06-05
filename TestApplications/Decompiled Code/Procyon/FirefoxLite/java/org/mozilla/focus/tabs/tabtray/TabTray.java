// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.tabs.tabtray;

import android.support.v4.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

public class TabTray
{
    public static void dismiss(final FragmentManager fragmentManager) {
        final Fragment fragmentByTag = fragmentManager.findFragmentByTag("tab_tray");
        if (fragmentByTag != null) {
            ((DialogFragment)fragmentByTag).dismissAllowingStateLoss();
        }
    }
    
    public static boolean isShowing(final FragmentManager fragmentManager) {
        return fragmentManager != null && fragmentManager.findFragmentByTag("tab_tray") != null;
    }
    
    public static void show(final FragmentManager fragmentManager) {
        if (!fragmentManager.isStateSaved()) {
            TabTrayFragment.newInstance().show(fragmentManager, "tab_tray");
        }
    }
}
