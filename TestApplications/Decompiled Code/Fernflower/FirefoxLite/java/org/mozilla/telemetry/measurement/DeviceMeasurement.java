package org.mozilla.telemetry.measurement;

import android.os.Build;
import org.mozilla.telemetry.util.StringUtils;

public class DeviceMeasurement extends TelemetryMeasurement {
   public DeviceMeasurement() {
      super("device");
   }

   public Object flush() {
      StringBuilder var1 = new StringBuilder();
      var1.append(StringUtils.safeSubstring(this.getManufacturer(), 0, 12));
      var1.append('-');
      var1.append(StringUtils.safeSubstring(this.getModel(), 0, 19));
      return var1.toString();
   }

   String getManufacturer() {
      return Build.MANUFACTURER;
   }

   String getModel() {
      return Build.MODEL;
   }
}
