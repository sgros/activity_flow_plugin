package org.mozilla.telemetry.measurement;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import org.mozilla.telemetry.config.TelemetryConfiguration;
import org.mozilla.telemetry.ping.TelemetryPingBuilder;

public class SequenceMeasurement extends TelemetryMeasurement {
   private final TelemetryConfiguration configuration;
   private final String preferenceKeySequence;

   public SequenceMeasurement(TelemetryConfiguration var1, TelemetryPingBuilder var2) {
      super("seq");
      this.configuration = var1;
      StringBuilder var3 = new StringBuilder();
      var3.append("sequence_");
      var3.append(var2.getType());
      this.preferenceKeySequence = var3.toString();
   }

   private long getAndIncrementSequence() {
      synchronized(this){}

      Throwable var10000;
      label72: {
         boolean var10001;
         long var2;
         Editor var4;
         String var11;
         try {
            SharedPreferences var1 = this.configuration.getSharedPreferences();
            var2 = var1.getLong(this.preferenceKeySequence, 0L);
            var4 = var1.edit();
            var11 = this.preferenceKeySequence;
         } catch (Throwable var10) {
            var10000 = var10;
            var10001 = false;
            break label72;
         }

         ++var2;

         label63:
         try {
            var4.putLong(var11, var2).apply();
            return var2;
         } catch (Throwable var9) {
            var10000 = var9;
            var10001 = false;
            break label63;
         }
      }

      Throwable var12 = var10000;
      throw var12;
   }

   public Object flush() {
      return this.getAndIncrementSequence();
   }
}
