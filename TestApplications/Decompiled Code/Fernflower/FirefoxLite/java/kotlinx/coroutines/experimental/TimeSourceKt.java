package kotlinx.coroutines.experimental;

public final class TimeSourceKt {
   private static TimeSource timeSource;

   static {
      timeSource = (TimeSource)DefaultTimeSource.INSTANCE;
   }

   public static final TimeSource getTimeSource() {
      return timeSource;
   }
}
