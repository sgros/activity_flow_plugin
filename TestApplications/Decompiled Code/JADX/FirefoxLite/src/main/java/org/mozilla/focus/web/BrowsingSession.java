package org.mozilla.focus.web;

import java.lang.ref.WeakReference;

public class BrowsingSession {
    private static BrowsingSession instance;
    private int blockedTrackers;
    private WeakReference<TrackingCountListener> listenerWeakReference = new WeakReference(null);

    public interface TrackingCountListener {
        void onTrackingCountChanged(int i);
    }

    public static synchronized BrowsingSession getInstance() {
        BrowsingSession browsingSession;
        synchronized (BrowsingSession.class) {
            if (instance == null) {
                instance = new BrowsingSession();
            }
            browsingSession = instance;
        }
        return browsingSession;
    }

    private BrowsingSession() {
    }

    public void countBlockedTracker() {
        this.blockedTrackers++;
        TrackingCountListener trackingCountListener = (TrackingCountListener) this.listenerWeakReference.get();
        if (trackingCountListener != null) {
            trackingCountListener.onTrackingCountChanged(this.blockedTrackers);
        }
    }

    public void resetTrackerCount() {
        this.blockedTrackers = 0;
    }
}
