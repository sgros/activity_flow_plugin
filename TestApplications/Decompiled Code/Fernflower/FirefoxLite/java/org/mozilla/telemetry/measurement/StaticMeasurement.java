package org.mozilla.telemetry.measurement;

public class StaticMeasurement extends TelemetryMeasurement {
   private final Object value;

   public StaticMeasurement(String var1, Object var2) {
      super(var1);
      this.value = var2;
   }

   public Object flush() {
      return this.value;
   }
}
