package org.mozilla.telemetry.measurement;

import org.json.JSONArray;

public class ExperimentsMeasurement extends TelemetryMeasurement {
   private JSONArray experimentsIds = new JSONArray();

   public ExperimentsMeasurement() {
      super("experiments");
   }

   public Object flush() {
      return this.experimentsIds;
   }
}
