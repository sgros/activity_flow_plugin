package org.mozilla.rocket.content;

public final class HomeFragmentViewState {
   public static final HomeFragmentViewState INSTANCE = new HomeFragmentViewState();
   private static Integer lastScrollPos;
   private static int state;

   private HomeFragmentViewState() {
   }

   public static final void lastOpenNews() {
      state = 1;
   }

   public static final void reset() {
      state = 0;
      lastScrollPos = (Integer)null;
   }

   public final Integer getLastScrollPos() {
      return lastScrollPos;
   }

   public final boolean isLastOpenNews() {
      int var1 = state;
      boolean var2 = true;
      if (var1 != 1) {
         var2 = false;
      }

      return var2;
   }

   public final void setLastScrollPos(Integer var1) {
      lastScrollPos = var1;
   }
}
