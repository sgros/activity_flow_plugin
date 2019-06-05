package org.mozilla.telemetry.measurement;

public abstract class TelemetryMeasurement {
   private final String fieldName;

   public TelemetryMeasurement(String var1) {
      this.fieldName = var1;
   }

   public abstract Object flush();

   public String getFieldName() {
      return this.fieldName;
   }
}
