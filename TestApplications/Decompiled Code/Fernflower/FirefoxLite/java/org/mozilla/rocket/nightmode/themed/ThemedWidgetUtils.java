package org.mozilla.rocket.nightmode.themed;

public final class ThemedWidgetUtils {
   public static final ThemedWidgetUtils INSTANCE = new ThemedWidgetUtils();
   private static final int[] STATE_NIGHT_MODE = new int[]{2130969045};

   private ThemedWidgetUtils() {
   }

   public final int[] getSTATE_NIGHT_MODE() {
      return STATE_NIGHT_MODE;
   }
}
