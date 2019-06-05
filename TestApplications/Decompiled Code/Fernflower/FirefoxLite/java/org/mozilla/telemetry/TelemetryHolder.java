package org.mozilla.telemetry;

public class TelemetryHolder {
   private static Telemetry telemetry;

   public static Telemetry get() {
      if (telemetry != null) {
         return telemetry;
      } else {
         throw new IllegalStateException("You need to call set() on TelemetryHolder in your application class");
      }
   }

   public static void set(Telemetry var0) {
      telemetry = var0;
   }
}
