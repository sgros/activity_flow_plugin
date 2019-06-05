package org.mozilla.telemetry.measurement;

public class CreatedTimestampMeasurement extends TelemetryMeasurement {
   public CreatedTimestampMeasurement() {
      super("created");
   }

   public Object flush() {
      return System.currentTimeMillis();
   }
}
