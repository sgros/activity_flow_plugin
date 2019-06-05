package org.mozilla.focus.widget;

import android.support.p001v4.app.Fragment;
import android.support.p001v4.app.FragmentActivity;

public interface FragmentListener {

    /* renamed from: org.mozilla.focus.widget.FragmentListener$-CC */
    public final /* synthetic */ class C0572-CC {
        public static void notifyParent(Fragment fragment, TYPE type, Object obj) {
            FragmentActivity activity = fragment.getActivity();
            if (activity instanceof FragmentListener) {
                ((FragmentListener) activity).onNotified(fragment, type, obj);
            }
        }
    }

    public enum TYPE {
        OPEN_PREFERENCE,
        OPEN_URL_IN_CURRENT_TAB,
        OPEN_URL_IN_NEW_TAB,
        SHOW_URL_INPUT,
        SHOW_MENU,
        DISMISS_URL_INPUT,
        UPDATE_MENU,
        REFRESH_TOP_SITE,
        TOGGLE_PRIVATE_MODE,
        DROP_BROWSING_PAGES,
        SHOW_TAB_TRAY,
        SHOW_MY_SHOT_ON_BOARDING,
        SHOW_DOWNLOAD_PANEL
    }

    void onNotified(Fragment fragment, TYPE type, Object obj);
}
