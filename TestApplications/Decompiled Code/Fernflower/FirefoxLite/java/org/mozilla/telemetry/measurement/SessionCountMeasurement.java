package org.mozilla.telemetry.measurement;

import android.content.SharedPreferences;
import org.mozilla.telemetry.config.TelemetryConfiguration;

public class SessionCountMeasurement extends TelemetryMeasurement {
   private final TelemetryConfiguration configuration;

   public SessionCountMeasurement(TelemetryConfiguration var1) {
      super("sessions");
      this.configuration = var1;
   }

   private long getAndResetCount() {
      synchronized(this){}

      long var2;
      try {
         SharedPreferences var1 = this.configuration.getSharedPreferences();
         var2 = var1.getLong("session_count", 0L);
         var1.edit().putLong("session_count", 0L).apply();
      } finally {
         ;
      }

      return var2;
   }

   public void countSession() {
      synchronized(this){}

      try {
         SharedPreferences var1 = this.configuration.getSharedPreferences();
         long var2 = var1.getLong("session_count", 0L);
         var1.edit().putLong("session_count", var2 + 1L).apply();
      } finally {
         ;
      }

   }

   public Object flush() {
      return this.getAndResetCount();
   }
}
