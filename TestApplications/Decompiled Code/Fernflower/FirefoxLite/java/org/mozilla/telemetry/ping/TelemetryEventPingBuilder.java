package org.mozilla.telemetry.ping;

import org.mozilla.telemetry.config.TelemetryConfiguration;
import org.mozilla.telemetry.measurement.CreatedTimestampMeasurement;
import org.mozilla.telemetry.measurement.EventsMeasurement;
import org.mozilla.telemetry.measurement.LocaleMeasurement;
import org.mozilla.telemetry.measurement.OperatingSystemMeasurement;
import org.mozilla.telemetry.measurement.OperatingSystemVersionMeasurement;
import org.mozilla.telemetry.measurement.SequenceMeasurement;
import org.mozilla.telemetry.measurement.SettingsMeasurement;
import org.mozilla.telemetry.measurement.TimezoneOffsetMeasurement;

@Deprecated
public class TelemetryEventPingBuilder extends TelemetryPingBuilder {
   private EventsMeasurement eventsMeasurement;

   public TelemetryEventPingBuilder(TelemetryConfiguration var1) {
      super(var1, "focus-event", 1);
      this.addMeasurement(new SequenceMeasurement(var1, this));
      this.addMeasurement(new LocaleMeasurement());
      this.addMeasurement(new OperatingSystemMeasurement());
      this.addMeasurement(new OperatingSystemVersionMeasurement());
      this.addMeasurement(new CreatedTimestampMeasurement());
      this.addMeasurement(new TimezoneOffsetMeasurement());
      this.addMeasurement(new SettingsMeasurement(var1));
      EventsMeasurement var2 = new EventsMeasurement(var1);
      this.eventsMeasurement = var2;
      this.addMeasurement(var2);
   }

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
