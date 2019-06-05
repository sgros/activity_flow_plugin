package org.mozilla.telemetry.measurement;

import org.json.JSONObject;

public class DefaultSearchMeasurement extends TelemetryMeasurement {
   private DefaultSearchMeasurement.DefaultSearchEngineProvider provider;

   public DefaultSearchMeasurement() {
      super("defaultSearch");
   }

   public Object flush() {
      if (this.provider == null) {
         return JSONObject.NULL;
      } else {
         Object var1 = this.provider.getDefaultSearchEngineIdentifier();
         if (var1 == null) {
            var1 = JSONObject.NULL;
         }

         return var1;
      }
   }

   public void setDefaultSearchEngineProvider(DefaultSearchMeasurement.DefaultSearchEngineProvider var1) {
      this.provider = var1;
   }

   public interface DefaultSearchEngineProvider {
      String getDefaultSearchEngineIdentifier();
   }
}
