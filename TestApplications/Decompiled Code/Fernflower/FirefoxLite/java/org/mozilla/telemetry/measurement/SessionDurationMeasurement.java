package org.mozilla.telemetry.measurement;

import android.content.SharedPreferences;
import java.util.concurrent.TimeUnit;
import org.mozilla.telemetry.config.TelemetryConfiguration;

public class SessionDurationMeasurement extends TelemetryMeasurement {
   private final TelemetryConfiguration configuration;
   private boolean sessionStarted = false;
   private long timeAtSessionStartNano = -1L;

   public SessionDurationMeasurement(TelemetryConfiguration var1) {
      super("durations");
      this.configuration = var1;
   }

   private long getAndResetDuration() {
      synchronized(this){}

      long var2;
      try {
         SharedPreferences var1 = this.configuration.getSharedPreferences();
         var2 = var1.getLong("session_duration", 0L);
         var1.edit().putLong("session_duration", 0L).apply();
      } finally {
         ;
      }

      return var2;
   }

   public Object flush() {
      return this.getAndResetDuration();
   }

   long getSystemTimeNano() {
      return System.nanoTime();
   }

   public boolean recordSessionEnd() {
      synchronized(this){}

      Throwable var10000;
      label78: {
         boolean var1;
         boolean var10001;
         try {
            var1 = this.sessionStarted;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label78;
         }

         if (!var1) {
            return false;
         }

         try {
            this.sessionStarted = false;
            long var2 = TimeUnit.NANOSECONDS.toSeconds(this.getSystemTimeNano() - this.timeAtSessionStartNano);
            SharedPreferences var13 = this.configuration.getSharedPreferences();
            long var5 = var13.getLong("session_duration", 0L);
            var13.edit().putLong("session_duration", var5 + var2).apply();
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            break label78;
         }

         return true;
      }

      Throwable var4 = var10000;
      throw var4;
   }

   public void recordSessionStart() {
      synchronized(this){}

      try {
         if (this.sessionStarted) {
            IllegalStateException var1 = new IllegalStateException("Trying to start session but it is already started");
            throw var1;
         }

         this.sessionStarted = true;
         this.timeAtSessionStartNano = this.getSystemTimeNano();
      } finally {
         ;
      }

   }
}
