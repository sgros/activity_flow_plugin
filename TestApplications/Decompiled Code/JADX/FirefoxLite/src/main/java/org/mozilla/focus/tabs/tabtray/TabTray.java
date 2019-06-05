package org.mozilla.focus.tabs.tabtray;

import android.support.p001v4.app.DialogFragment;
import android.support.p001v4.app.Fragment;
import android.support.p001v4.app.FragmentManager;

public class TabTray {
    public static void show(FragmentManager fragmentManager) {
        if (!fragmentManager.isStateSaved()) {
            TabTrayFragment.newInstance().show(fragmentManager, "tab_tray");
        }
    }

    public static void dismiss(FragmentManager fragmentManager) {
        Fragment findFragmentByTag = fragmentManager.findFragmentByTag("tab_tray");
        if (findFragmentByTag != null) {
            ((DialogFragment) findFragmentByTag).dismissAllowingStateLoss();
        }
    }

    public static boolean isShowing(FragmentManager fragmentManager) {
        return (fragmentManager == null || fragmentManager.findFragmentByTag("tab_tray") == null) ? false : true;
    }
}
