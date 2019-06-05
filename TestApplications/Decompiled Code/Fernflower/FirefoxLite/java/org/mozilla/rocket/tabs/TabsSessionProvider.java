package org.mozilla.rocket.tabs;

import android.app.Activity;

public final class TabsSessionProvider {
   public static SessionManager getOrNull(Activity var0) {
      try {
         SessionManager var2 = getOrThrow(var0);
         return var2;
      } catch (Exception var1) {
         return null;
      }
   }

   public static SessionManager getOrThrow(Activity var0) throws IllegalArgumentException {
      if (var0 instanceof TabsSessionProvider.SessionHost) {
         return ((TabsSessionProvider.SessionHost)var0).getSessionManager();
      } else {
         throw new IllegalArgumentException("activity must implement TabsSessionProvider.SessionHost");
      }
   }

   public interface SessionHost {
      SessionManager getSessionManager();
   }
}
