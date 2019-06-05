// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.widget;

import android.support.v4.app.Fragment;

public interface FragmentListener
{
    void onNotified(final Fragment p0, final TYPE p1, final Object p2);
    
    public enum TYPE
    {
        DISMISS_URL_INPUT, 
        DROP_BROWSING_PAGES, 
        OPEN_PREFERENCE, 
        OPEN_URL_IN_CURRENT_TAB, 
        OPEN_URL_IN_NEW_TAB, 
        REFRESH_TOP_SITE, 
        SHOW_DOWNLOAD_PANEL, 
        SHOW_MENU, 
        SHOW_MY_SHOT_ON_BOARDING, 
        SHOW_TAB_TRAY, 
        SHOW_URL_INPUT, 
        TOGGLE_PRIVATE_MODE, 
        UPDATE_MENU;
    }
}
