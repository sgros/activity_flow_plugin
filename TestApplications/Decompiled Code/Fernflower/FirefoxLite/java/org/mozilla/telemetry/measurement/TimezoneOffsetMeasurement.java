package org.mozilla.telemetry.measurement;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class TimezoneOffsetMeasurement extends TelemetryMeasurement {
   public TimezoneOffsetMeasurement() {
      super("tz");
   }

   private static int getTimezoneOffsetInMinutesForGivenDate(Calendar var0) {
      return (int)TimeUnit.MILLISECONDS.toMinutes((long)(var0.get(15) + var0.get(16)));
   }

   public Object flush() {
      return getTimezoneOffsetInMinutesForGivenDate(this.now());
   }

   Calendar now() {
      return Calendar.getInstance();
   }
}
