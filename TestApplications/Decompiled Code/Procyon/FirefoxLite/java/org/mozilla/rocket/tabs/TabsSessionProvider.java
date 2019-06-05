// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.tabs;

import android.app.Activity;

public final class TabsSessionProvider
{
    public static SessionManager getOrNull(final Activity activity) {
        try {
            return getOrThrow(activity);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static SessionManager getOrThrow(final Activity activity) throws IllegalArgumentException {
        if (activity instanceof SessionHost) {
            return ((SessionHost)activity).getSessionManager();
        }
        throw new IllegalArgumentException("activity must implement TabsSessionProvider.SessionHost");
    }
    
    public interface SessionHost
    {
        SessionManager getSessionManager();
    }
}
