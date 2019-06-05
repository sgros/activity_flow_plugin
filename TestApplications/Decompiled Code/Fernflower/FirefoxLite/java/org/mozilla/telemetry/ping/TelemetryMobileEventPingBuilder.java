package org.mozilla.telemetry.ping;

import org.mozilla.telemetry.measurement.EventsMeasurement;

public class TelemetryMobileEventPingBuilder extends TelemetryPingBuilder {
   private EventsMeasurement eventsMeasurement;

   public boolean canBuild() {
      boolean var1;
      if (this.eventsMeasurement.getEventCount() >= (long)this.getConfiguration().getMinimumEventsForUpload()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public EventsMeasurement getEventsMeasurement() {
      return this.eventsMeasurement;
   }

   protected String getUploadPath(String var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append(super.getUploadPath(var1));
      var2.append("?v=4");
      return var2.toString();
   }
}
