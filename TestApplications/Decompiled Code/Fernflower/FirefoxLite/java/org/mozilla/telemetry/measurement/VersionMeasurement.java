package org.mozilla.telemetry.measurement;

public class VersionMeasurement extends StaticMeasurement {
   public VersionMeasurement(int var1) {
      super("v", var1);
      if (var1 <= 0) {
         throw new IllegalArgumentException("Version should be a positive integer > 0");
      }
   }
}
