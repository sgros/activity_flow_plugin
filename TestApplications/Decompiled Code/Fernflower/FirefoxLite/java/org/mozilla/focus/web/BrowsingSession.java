package org.mozilla.focus.web;

import java.lang.ref.WeakReference;

public class BrowsingSession {
   private static BrowsingSession instance;
   private int blockedTrackers;
   private WeakReference listenerWeakReference = new WeakReference((Object)null);

   private BrowsingSession() {
   }

   public static BrowsingSession getInstance() {
      synchronized(BrowsingSession.class){}

      BrowsingSession var0;
      try {
         if (instance == null) {
            var0 = new BrowsingSession();
            instance = var0;
         }

         var0 = instance;
      } finally {
         ;
      }

      return var0;
   }

   public void countBlockedTracker() {
      ++this.blockedTrackers;
      BrowsingSession.TrackingCountListener var1 = (BrowsingSession.TrackingCountListener)this.listenerWeakReference.get();
      if (var1 != null) {
         var1.onTrackingCountChanged(this.blockedTrackers);
      }

   }

   public void resetTrackerCount() {
      this.blockedTrackers = 0;
   }

   public interface TrackingCountListener {
      void onTrackingCountChanged(int var1);
   }
}
