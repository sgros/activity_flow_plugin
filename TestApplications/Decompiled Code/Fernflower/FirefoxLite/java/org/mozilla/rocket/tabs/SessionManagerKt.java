package org.mozilla.rocket.tabs;

public final class SessionManagerKt {
   private static final int MSG_ADDED_TAB = 4098;
   private static final int MSG_FOCUS_TAB = 4097;
   private static final int MSG_REMOVEDED_TAB = 4099;

   public static final int getMSG_ADDED_TAB() {
      return MSG_ADDED_TAB;
   }

   public static final int getMSG_FOCUS_TAB() {
      return MSG_FOCUS_TAB;
   }

   public static final int getMSG_REMOVEDED_TAB() {
      return MSG_REMOVEDED_TAB;
   }
}