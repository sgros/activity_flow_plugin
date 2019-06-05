package org.mozilla.rocket.tabs;

import android.app.Activity;

public final class TabsSessionProvider {

    public interface SessionHost {
        SessionManager getSessionManager();
    }

    public static SessionManager getOrThrow(Activity activity) throws IllegalArgumentException {
        if (activity instanceof SessionHost) {
            return ((SessionHost) activity).getSessionManager();
        }
        throw new IllegalArgumentException("activity must implement TabsSessionProvider.SessionHost");
    }

    public static SessionManager getOrNull(Activity activity) {
        try {
            return getOrThrow(activity);
        } catch (Exception unused) {
            return null;
        }
    }
}
