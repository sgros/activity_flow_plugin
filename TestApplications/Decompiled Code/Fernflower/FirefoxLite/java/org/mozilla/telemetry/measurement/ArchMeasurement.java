package org.mozilla.telemetry.measurement;

import android.os.Build;
import android.os.Build.VERSION;

public class ArchMeasurement extends TelemetryMeasurement {
   public ArchMeasurement() {
      super("arch");
   }

   public Object flush() {
      return this.getArchitecture();
   }

   String getArchitecture() {
      return VERSION.SDK_INT >= 21 ? Build.SUPPORTED_ABIS[0] : Build.CPU_ABI;
   }
}
