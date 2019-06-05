package org.mozilla.telemetry.measurement;

import android.content.SharedPreferences;
import java.util.concurrent.TimeUnit;
import org.mozilla.telemetry.config.TelemetryConfiguration;

public class FirstRunProfileDateMeasurement extends TelemetryMeasurement {
   private TelemetryConfiguration configuration;

   public FirstRunProfileDateMeasurement(TelemetryConfiguration var1) {
      super("profileDate");
      this.configuration = var1;
      this.ensureValueExists();
   }

   private long getProfileDateInDays() {
      return (long)Math.floor((double)this.configuration.getSharedPreferences().getLong("profile_date", this.now()) / (double)TimeUnit.DAYS.toMillis(1L));
   }

   void ensureValueExists() {
      SharedPreferences var1 = this.configuration.getSharedPreferences();
      if (!var1.contains("profile_date")) {
         var1.edit().putLong("profile_date", this.now()).apply();
      }
   }

   public Object flush() {
      return this.getProfileDateInDays();
   }

   long now() {
      return System.currentTimeMillis();
   }
}
