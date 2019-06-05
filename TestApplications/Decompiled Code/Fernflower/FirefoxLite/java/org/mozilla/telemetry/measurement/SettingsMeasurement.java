package org.mozilla.telemetry.measurement;

import android.preference.PreferenceManager;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.telemetry.config.TelemetryConfiguration;

public class SettingsMeasurement extends TelemetryMeasurement {
   private final TelemetryConfiguration configuration;

   public SettingsMeasurement(TelemetryConfiguration var1) {
      super("settings");
      this.configuration = var1;
   }

   public Object flush() {
      SettingsMeasurement.SettingsProvider var1 = this.configuration.getSettingsProvider();
      var1.update(this.configuration);
      JSONObject var2 = new JSONObject();
      Set var3 = this.configuration.getPreferencesImportantForTelemetry();
      if (var3.isEmpty()) {
         return var2;
      } else {
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            String var6 = (String)var4.next();

            try {
               if (var1.containsKey(var6)) {
                  var2.put(var6, String.valueOf(var1.getValue(var6)));
               } else {
                  var2.put(var6, JSONObject.NULL);
               }
            } catch (JSONException var5) {
               throw new AssertionError("Preference value can't be serialized to JSON", var5);
            }
         }

         var1.release();
         return var2;
      }
   }

   public interface SettingsProvider {
      boolean containsKey(String var1);

      Object getValue(String var1);

      void release();

      void update(TelemetryConfiguration var1);
   }

   public static class SharedPreferenceSettingsProvider implements SettingsMeasurement.SettingsProvider {
      private Map preferences;

      public boolean containsKey(String var1) {
         boolean var2;
         if (this.preferences != null && this.preferences.containsKey(var1)) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      public Object getValue(String var1) {
         return this.preferences.get(var1);
      }

      public void release() {
         this.preferences = null;
      }

      public void update(TelemetryConfiguration var1) {
         this.preferences = PreferenceManager.getDefaultSharedPreferences(var1.getContext()).getAll();
      }
   }
}
