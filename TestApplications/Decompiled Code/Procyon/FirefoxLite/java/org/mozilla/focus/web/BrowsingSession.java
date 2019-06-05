// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.web;

import java.lang.ref.WeakReference;

public class BrowsingSession
{
    private static BrowsingSession instance;
    private int blockedTrackers;
    private WeakReference<TrackingCountListener> listenerWeakReference;
    
    private BrowsingSession() {
        this.listenerWeakReference = new WeakReference<TrackingCountListener>(null);
    }
    
    public static BrowsingSession getInstance() {
        synchronized (BrowsingSession.class) {
            if (BrowsingSession.instance == null) {
                BrowsingSession.instance = new BrowsingSession();
            }
            return BrowsingSession.instance;
        }
    }
    
    public void countBlockedTracker() {
        ++this.blockedTrackers;
        final TrackingCountListener trackingCountListener = this.listenerWeakReference.get();
        if (trackingCountListener != null) {
            trackingCountListener.onTrackingCountChanged(this.blockedTrackers);
        }
    }
    
    public void resetTrackerCount() {
        this.blockedTrackers = 0;
    }
    
    public interface TrackingCountListener
    {
        void onTrackingCountChanged(final int p0);
    }
}
