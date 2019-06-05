package mozilla.components.support.base.log.logger;

import kotlin.jvm.internal.DefaultConstructorMarker;
import mozilla.components.support.base.log.Log;

public final class Logger {
   public static final Logger.Companion Companion = new Logger.Companion((DefaultConstructorMarker)null);
   private static final Logger DEFAULT = new Logger((String)null, 1, (DefaultConstructorMarker)null);
   private final String tag;

   public Logger() {
      this((String)null, 1, (DefaultConstructorMarker)null);
   }

   public Logger(String var1) {
      this.tag = var1;
   }

   // $FF: synthetic method
   public Logger(String var1, int var2, DefaultConstructorMarker var3) {
      if ((var2 & 1) != 0) {
         var1 = (String)null;
      }

      this(var1);
   }

   public final void debug(String var1, Throwable var2) {
      Log.INSTANCE.log(Log.Priority.DEBUG, this.tag, var2, var1);
   }

   public final void error(String var1, Throwable var2) {
      Log.INSTANCE.log(Log.Priority.ERROR, this.tag, var2, var1);
   }

   public final void info(String var1, Throwable var2) {
      Log.INSTANCE.log(Log.Priority.INFO, this.tag, var2, var1);
   }

   public final void warn(String var1, Throwable var2) {
      Log.INSTANCE.log(Log.Priority.WARN, this.tag, var2, var1);
   }

   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }
   }
}
