package mozilla.components.support.base.log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import mozilla.components.support.base.log.sink.LogSink;

public final class Log {
   public static final Log INSTANCE = new Log();
   private static Log.Priority logLevel;
   private static final List sinks;

   static {
      logLevel = Log.Priority.DEBUG;
      sinks = (List)(new ArrayList());
   }

   private Log() {
   }

   public final void log(Log.Priority var1, String var2, Throwable var3, String var4) {
      Intrinsics.checkParameterIsNotNull(var1, "priority");
      if (var1.getValue() >= logLevel.getValue()) {
         List var5 = sinks;
         synchronized(var5){}

         Throwable var10000;
         label164: {
            Iterator var6;
            boolean var10001;
            try {
               var6 = ((Iterable)sinks).iterator();
            } catch (Throwable var17) {
               var10000 = var17;
               var10001 = false;
               break label164;
            }

            while(true) {
               try {
                  if (!var6.hasNext()) {
                     break;
                  }

                  ((LogSink)var6.next()).log(var1, var2, var3, var4);
               } catch (Throwable var18) {
                  var10000 = var18;
                  var10001 = false;
                  break label164;
               }
            }

            label146:
            try {
               Unit var20 = Unit.INSTANCE;
               return;
            } catch (Throwable var16) {
               var10000 = var16;
               var10001 = false;
               break label146;
            }
         }

         Throwable var19 = var10000;
         throw var19;
      }
   }

   public static enum Priority {
      DEBUG,
      ERROR,
      INFO,
      WARN;

      private final int value;

      static {
         Log.Priority var0 = new Log.Priority("DEBUG", 0, 3);
         DEBUG = var0;
         Log.Priority var1 = new Log.Priority("INFO", 1, 4);
         INFO = var1;
         Log.Priority var2 = new Log.Priority("WARN", 2, 5);
         WARN = var2;
         Log.Priority var3 = new Log.Priority("ERROR", 3, 6);
         ERROR = var3;
      }

      protected Priority(int var3) {
         this.value = var3;
      }

      public final int getValue() {
         return this.value;
      }
   }
}
