package org.mozilla.telemetry.ping;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.mozilla.telemetry.config.TelemetryConfiguration;
import org.mozilla.telemetry.measurement.ClientIdMeasurement;
import org.mozilla.telemetry.measurement.TelemetryMeasurement;
import org.mozilla.telemetry.measurement.VersionMeasurement;

public abstract class TelemetryPingBuilder {
   private TelemetryConfiguration configuration;
   private final List measurements;
   private final String type;

   public TelemetryPingBuilder(TelemetryConfiguration var1, String var2, int var3) {
      this.configuration = var1;
      this.type = var2;
      this.measurements = new LinkedList();
      this.addMeasurement(new VersionMeasurement(var3));
      this.addMeasurement(new ClientIdMeasurement(var1));
   }

   private Map flushMeasurements() {
      LinkedHashMap var1 = new LinkedHashMap();
      Iterator var2 = this.measurements.iterator();

      while(var2.hasNext()) {
         TelemetryMeasurement var3 = (TelemetryMeasurement)var2.next();
         var1.put(var3.getFieldName(), var3.flush());
      }

      return var1;
   }

   protected void addMeasurement(TelemetryMeasurement var1) {
      this.measurements.add(var1);
   }

   public TelemetryPing build() {
      String var1 = this.generateDocumentId();
      return new TelemetryPing(this.getType(), var1, this.getUploadPath(var1), this.flushMeasurements());
   }

   public boolean canBuild() {
      return true;
   }

   public String generateDocumentId() {
      return UUID.randomUUID().toString();
   }

   public TelemetryConfiguration getConfiguration() {
      return this.configuration;
   }

   public String getType() {
      return this.type;
   }

   protected String getUploadPath(String var1) {
      TelemetryConfiguration var2 = this.getConfiguration();
      return String.format("/submit/telemetry/%s/%s/%s/%s/%s/%s", var1, this.getType(), var2.getAppName(), var2.getAppVersion(), var2.getUpdateChannel(), var2.getBuildId());
   }
}
