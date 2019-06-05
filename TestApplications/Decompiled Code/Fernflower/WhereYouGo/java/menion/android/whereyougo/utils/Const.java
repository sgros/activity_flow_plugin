package menion.android.whereyougo.utils;

public class Const {
   public static final int PRECISION = 5;
   private static final int PUBLISH_PRIVATE = 2;
   private static final int PUBLISH_RELEASE = 0;
   private static final int RELEASE = 2;
   public static final float RHO = 57.29578F;
   public static int SCREEN_HEIGHT = 0;
   public static final int SCREEN_SIZE_LARGE = 2;
   public static final int SCREEN_SIZE_MEDIUM = 1;
   public static final int SCREEN_SIZE_SMALL = 0;
   public static final int SCREEN_SIZE_XLARGE = 3;
   public static int SCREEN_WIDTH = 0;
   public static boolean STATE_DEBUG_LOGS;
   public static boolean STATE_RELEASE;
   public static final int TEXT_SIZE_BIG = 2;
   public static final int TEXT_SIZE_HUGE = 3;
   public static final int TEXT_SIZE_MEDIUM = 1;
   public static final int TEXT_SIZE_SMALL = 0;

   static {
      switch(2) {
      case 0:
         STATE_RELEASE = true;
         STATE_DEBUG_LOGS = false;
      case 1:
      default:
         break;
      case 2:
         STATE_RELEASE = false;
         STATE_DEBUG_LOGS = true;
      }

   }
}
