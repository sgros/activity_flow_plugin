package org.mozilla.telemetry.ping;

import org.mozilla.telemetry.config.TelemetryConfiguration;
import org.mozilla.telemetry.measurement.ArchMeasurement;
import org.mozilla.telemetry.measurement.CreatedDateMeasurement;
import org.mozilla.telemetry.measurement.DefaultSearchMeasurement;
import org.mozilla.telemetry.measurement.DeviceMeasurement;
import org.mozilla.telemetry.measurement.ExperimentsMeasurement;
import org.mozilla.telemetry.measurement.FirstRunProfileDateMeasurement;
import org.mozilla.telemetry.measurement.LocaleMeasurement;
import org.mozilla.telemetry.measurement.OperatingSystemMeasurement;
import org.mozilla.telemetry.measurement.OperatingSystemVersionMeasurement;
import org.mozilla.telemetry.measurement.SearchesMeasurement;
import org.mozilla.telemetry.measurement.SequenceMeasurement;
import org.mozilla.telemetry.measurement.SessionCountMeasurement;
import org.mozilla.telemetry.measurement.SessionDurationMeasurement;
import org.mozilla.telemetry.measurement.TimezoneOffsetMeasurement;

public class TelemetryCorePingBuilder extends TelemetryPingBuilder {
   private DefaultSearchMeasurement defaultSearchMeasurement;
   private ExperimentsMeasurement experimentsMeasurement;
   private SearchesMeasurement searchesMeasurement;
   private SessionCountMeasurement sessionCountMeasurement;
   private SessionDurationMeasurement sessionDurationMeasurement;

   public TelemetryCorePingBuilder(TelemetryConfiguration var1) {
      super(var1, "core", 7);
      this.addMeasurement(new SequenceMeasurement(var1, this));
      this.addMeasurement(new LocaleMeasurement());
      this.addMeasurement(new OperatingSystemMeasurement());
      this.addMeasurement(new OperatingSystemVersionMeasurement());
      this.addMeasurement(new DeviceMeasurement());
      this.addMeasurement(new ArchMeasurement());
      this.addMeasurement(new FirstRunProfileDateMeasurement(var1));
      DefaultSearchMeasurement var2 = new DefaultSearchMeasurement();
      this.defaultSearchMeasurement = var2;
      this.addMeasurement(var2);
      this.addMeasurement(new CreatedDateMeasurement());
      this.addMeasurement(new TimezoneOffsetMeasurement());
      SessionCountMeasurement var5 = new SessionCountMeasurement(var1);
      this.sessionCountMeasurement = var5;
      this.addMeasurement(var5);
      SessionDurationMeasurement var6 = new SessionDurationMeasurement(var1);
      this.sessionDurationMeasurement = var6;
      this.addMeasurement(var6);
      SearchesMeasurement var3 = new SearchesMeasurement(var1);
      this.searchesMeasurement = var3;
      this.addMeasurement(var3);
      ExperimentsMeasurement var4 = new ExperimentsMeasurement();
      this.experimentsMeasurement = var4;
      this.addMeasurement(var4);
   }

   public DefaultSearchMeasurement getDefaultSearchMeasurement() {
      return this.defaultSearchMeasurement;
   }

   public SearchesMeasurement getSearchesMeasurement() {
      return this.searchesMeasurement;
   }

   public SessionCountMeasurement getSessionCountMeasurement() {
      return this.sessionCountMeasurement;
   }

   public SessionDurationMeasurement getSessionDurationMeasurement() {
      return this.sessionDurationMeasurement;
   }

   protected String getUploadPath(String var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append(super.getUploadPath(var1));
      var2.append("?v=4");
      return var2.toString();
   }
}
