package org.mozilla.telemetry.measurement;

import android.content.SharedPreferences;
import java.util.UUID;
import org.mozilla.telemetry.config.TelemetryConfiguration;

public class ClientIdMeasurement extends TelemetryMeasurement {
   private String clientId;
   private TelemetryConfiguration configuration;

   public ClientIdMeasurement(TelemetryConfiguration var1) {
      super("clientId");
      this.configuration = var1;
   }

   private static String generateClientId(TelemetryConfiguration var0) {
      synchronized(ClientIdMeasurement.class){}

      String var5;
      try {
         SharedPreferences var4 = var0.getSharedPreferences();
         if (!var4.contains("client_id")) {
            String var1 = UUID.randomUUID().toString();
            var4.edit().putString("client_id", var1).apply();
            return var1;
         }

         var5 = var4.getString("client_id", (String)null);
      } finally {
         ;
      }

      return var5;
   }

   public Object flush() {
      if (this.clientId == null) {
         this.clientId = generateClientId(this.configuration);
      }

      return this.clientId;
   }
}
